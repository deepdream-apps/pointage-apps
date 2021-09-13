package cm.deepdream.pointage.web.controlers;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import cm.deepdream.pointage.exceptions.PointageException;
import cm.deepdream.pointage.model.Session;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class PointageWebCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PointageWebCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;

	
	 @GetMapping ("/")
	 public String pageLogin (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/login")
	 public String pageLogin2 (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/my-account")
	 public String monCompte (Model model) {
	       return "admin/mon-compte" ;
	 }
	 
	 
	 @GetMapping ("/login-echec")
	 public String pageLogin3 (Model model) {
		 model.addAttribute("messageEchec", model.getAttribute("messageEchec") == null ? "Echec de l'opération" : model.getAttribute("messageEchec")) ;
	     return "login" ;
	 }
	 
	 
	 @GetMapping ("/forgot-password")
	 public String forgotPwd (Model model) {
	       return "forgot-password" ;
	 }
	 
	 @GetMapping ("/change-password")
	 public String changePwd (Model model) {
	       return "change-password" ;
	 }
	 
	 @GetMapping ("/disconnected")
	 public String disconnected (Model model) {
	       return "login" ;
	 }
	 
	 @GetMapping ("/error")
	 public String pageErreur (Model model) {
	       return "extra-404" ;
	 }
	 
	 @GetMapping ("/dashboard")
	 public String dashboard (Model model, HttpServletRequest request) throws Exception{
		 try {
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
			 String login = authentication.getName() ;
			 Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
			 
			 if (utilisateur == null) {
				 throw new PointageException ("Echec ! Login ou mot de passe incorrect") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpiration()).isNegative()) {
				 throw new PointageException ("Echec ! Mot de passe expiré") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpiration()).isNegative() ) {
				 throw new PointageException ("Echec ! Votre compte a expiré") ;
			 }
			 
			 model.addAttribute("utilisateurCourant", utilisateur) ;			
			 
			 utilisateur.setDateDernConn(LocalDateTime.now()) ;
			 rest.put("http://pointage-zuul/pointage-server/ws/utilisateur/maj", utilisateur) ;
			 
			 Session session = new Session() ;
			 session.setUtilisateur(utilisateur) ;
			 session.setDateDebut(LocalDateTime.now()) ;
			 session.setAdresseIP(request.getRemoteAddr());
			 rest.postForEntity("http://pointage-zuul/pointage-server/ws/session/ajout", session, Session.class);
		     return "dashboard" ;
		 }catch(SecurityException sex) {
			 model.addAttribute("messageEchec", sex.getMessage()) ;
			 logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			 return "login";
		 }catch(Exception ex) {
			 model.addAttribute("messageEchec", "Echec de l'opération") ;
			 logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			 return "login";
		 }
	 }
	 
	 @GetMapping ("/info-profil")
	 public String infoProfil (Model model, HttpServletRequest request)  throws Exception{
		/* Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://dreamscool-zuul/dreamscool-securite/utilisateur/{login}", Utilisateur.class, login) ;
		 model.addAttribute("utilisateurCourant", utilisateur) ;
		 Map<String,Long> urlVariables = new HashMap<>();
		 urlVariables.put("idUtilisateur", utilisateur.getId()) ;
		 List<Session> listeDernieresConnexions = rest.getForObject("http://dreamscool-zuul/dreamscool-securite/session/search/{idUtilisateur}", 
				 List.class, urlVariables) ;
		 model.addAttribute("listeDernieresConnexions", listeDernieresConnexions.subList(listeDernieresConnexions.size() - 5 >=0 ? listeDernieresConnexions.size()-5 : 0, 
				 listeDernieresConnexions.size())) ;*/
		 
	     return "info-profil" ;
	 }
	 
	 @GetMapping ("/maj-mot-de-passe")
	 public String initMajMdp (Model model, HttpServletRequest request)  throws Exception{
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
		 String login = authentication.getName() ;
		 Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
		 model.addAttribute("currentUser", utilisateur) ;
	     return "maj-mot-de-passe" ;
	 }
	 
	 @PostMapping ("/mot-de-passe/maj")
	 public String majMdp (Model model, Utilisateur utilisateurCourant) throws Exception{
		// String mdp1 = utilisateurCourant.getMdp1() ;
		// String mdp2 = utilisateurCourant.getMdp2() ;
		// String mdp3 = utilisateurCourant.getMdp3() ;
		 
		 /*  if(! new BCryptPasswordEncoder().encode(mdp1).equals(utilisateurCourant.getMotDePasse())) {
			 model.addAttribute("messageEchec", "Echec ! Ancien mot de passe incorrect") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		if(! mdp2.equals(mdp3)) {
			 model.addAttribute("messageEchec", "Confirmation de mot de passe incorrecte") ;
			 return "maj-mot-de-passe" ;
		 }
		 
		 utilisateurCourant.setMotDePasse(new BCryptPasswordEncoder().encode(mdp2)) ;*/
		 
		 rest.put("http://dreamscool-zuul/dreamscool-securite/utilisateur/{id}", utilisateurCourant, utilisateurCourant.getId()) ;
		 
		 model.addAttribute("messageSuccess", "Mot de passe mis jour") ;
	     return "maj-mot-de-passe" ;
	 }
	 
}
