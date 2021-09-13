package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.enums.TypeProfil;
import cm.deepdream.pointage.exceptions.PointageException;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class UtilisateurCtrl implements Serializable{
	private Logger logger = Logger.getLogger(UtilisateurCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/admin/utilisateurs")
	public String index (Model model) throws Exception {
		 ResponseEntity<Utilisateur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/utilisateur/all/", Utilisateur[].class) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		return "admin/utilisateurs" ;
	}
	

	@GetMapping ("/admin/ajout-utilisateur")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Utilisateur utilisateur = new Utilisateur() ;
		utilisateur.setDateCreation(LocalDateTime.now()) ;
		utilisateur.setDateDernMaj(LocalDateTime.now()) ;
		utilisateur.setCreateur(utilisateurCourant.getLogin());
		utilisateur.setModificateur(utilisateurCourant.getLogin());
		model.addAttribute("utilisateur", utilisateur) ;
		
		utilisateur.setDateExpirationMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
		utilisateur.setDateExpiration(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
		initDependencies(model) ;
		return "admin/ajout-utilisateur" ;
	}
	
	@PostMapping ("/admin/utilisateur/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setCreateur(utilisateurCourant.getLogin());
			utilisateur.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/utilisateur/ajout", utilisateur, Utilisateur.class);			
			return "redirect:/admin/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;
			model.addAttribute("utilisateur", utilisateur) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/ajout-utilisateur" ;
		}
	}
	
	@GetMapping ("/admin/maj-utilisateur/{idUtilisateur}")
	public String initMaj (Model model, @PathVariable("idUtilisateur") long idUtilisateur) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
				Utilisateur.class, idUtilisateur) ;
		model.addAttribute("utilisateurExistant", utilisateur) ;
		initDependencies(model) ;
		return "admin/maj-utilisateur" ;
	}
	
	@PostMapping ("/admin/utilisateur/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
					Utilisateur.class, utilisateurExistant.getId()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setModificateur(utilisateurExistant.getModificateur()) ;
			utilisateur.setEmploye(utilisateurExistant.getEmploye());
			utilisateur.setTelephone(utilisateurExistant.getTelephone());
			utilisateur.setEmail(utilisateurExistant.getEmail());
			utilisateur.setProfil(utilisateurExistant.getProfil());
			rest.put("http://pointage-zuul/pointage-server/ws/utilisateur/maj", utilisateur, Utilisateur.class);
			return "redirect:/admin/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/maj-utilisateur" ;
		}
	}
	
	@GetMapping ("/admin/details-utilisateur/{idUtilisateur}")
	public String initDetails (Model model, @PathVariable("idUtilisateur") long idUtilisateur) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
				Utilisateur.class, idUtilisateur) ;
		model.addAttribute("utilisateurExistant", utilisateur) ;
		return "admin/details-utilisateur" ;
	}
	
	
	@PostMapping ("/admin/utilisateur/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
					Utilisateur.class, utilisateurExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("idUtilisateur", utilisateur.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/utilisateur/suppr/{idUtilisateur}", uriVariables);
			return "redirect:/admin/utilisateurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/details-utilisateur" ;
		}
	}
	
	
	@GetMapping ("/admin/utilisateur/activate/{idUtilisateur}")
	public String activer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@PathVariable("idUtilisateur") long idUtilisateur) throws Exception {
		try {
			Utilisateur utilisateurExistant = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/activate/{id}", 
					Utilisateur.class, idUtilisateur) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			return "admin/details-utilisateur" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			Utilisateur utilisateurExistant = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
					Utilisateur.class, idUtilisateur) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/details-utilisateur" ;
		}
	}
	
	@GetMapping ("/admin/utilisateur/desactivate/{idUtilisateur}")
	public String desactiver (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@PathVariable("idUtilisateur") long idUtilisateur) throws Exception {
		try {
			Utilisateur utilisateurExistant = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/desactivate/{id}", 
					Utilisateur.class, idUtilisateur) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			return "admin/details-utilisateur" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			Utilisateur utilisateurExistant = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{idUtilisateur}", 
					Utilisateur.class, idUtilisateur) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/details-utilisateur" ;
		}
	}
	
	@GetMapping ("/admin/maj-mot-de-passe-1/{idUtilisateur}")
	public String majMdp1 (Model model, @PathVariable("idUtilisateur") long id) throws Exception {
		Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{id}", Utilisateur.class, id) ;
		if((utilisateur != null && utilisateur.getEtat() == 1) || utilisateur == null) {
			return "login" ;
		} else if(utilisateur.getEtat() == 3) {
			model.addAttribute("utilisateur", utilisateur) ;
			return "admin/maj-mot-de-passe-1" ;
		}
		return "admin/maj-mot-de-passe-1" ;
	}
	
	@PostMapping ("/admin/maj-mot-de-passe-1")
	public String majMdp2 (Model model, Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{id}", 
				Utilisateur.class, utilisateurExistant.getId()) ;
			utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(utilisateurExistant.getMdp1())) ;
			rest.put("http://pointage-zuul/pointage-server/ws/utilisateur/maj-mot-de-passe", utilisateur, Utilisateur.class) ;
			model.addAttribute("messageSucces", "Mot de passe modifié") ;
			return "admin/maj-mot-de-passe-1" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/maj-mot-de-passe-1" ;
		}
	}
	
	@GetMapping ("/admin/maj-mot-de-passe")
	public String initMajMdp (Model model, @SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourante) throws Exception {
		model.addAttribute("utilisateurExistant", utilisateurCourante) ;
		return "admin/maj-mot-de-passe" ;
	}
	
	@PostMapping ("/admin/maj-mot-de-passe")
	public String majMdp (Model model, Utilisateur utilisateurExistant) throws Exception {
		try {
			Utilisateur utilisateur = rest.getForObject("http://pointage-zuul/pointage-server/ws/utilisateur/id/{id}", Utilisateur.class, utilisateurExistant.getId()) ;
			
			if(! new BCryptPasswordEncoder().encode(utilisateurExistant.getMdp1()).equals(utilisateur.getMotDePasse())) {
				throw new PointageException ("Ancien mot de passe invalide") ;
			}
			
			if(! utilisateurExistant.getMdp3().equals(utilisateurExistant.getMdp2())) {
				throw new PointageException ("Erreur de confirmation du mot de passe") ;
			}
			
			utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(utilisateurExistant.getMdp2())) ;
			rest.put("http://pointage-zuul/pointage-server/ws/utilisateur/maj-mot-de-passe", utilisateur, Utilisateur.class) ;
			model.addAttribute("messageSucces", "Mot de passe modifié") ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			return "admin/maj-mot-de-passe" ;
		}catch(PointageException sex) {
			logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", sex.getMessage()) ;
			return "admin/maj-mot-de-passe" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("utilisateurExistant", utilisateurExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/maj-mot-de-passe" ;
		}
	}
	
	private void initDependencies(Model model) {
		 ResponseEntity<Profil[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/profil/all/", Profil[].class) ;
		 List<Profil> listeProfils = Arrays.asList(response.getBody());
		 model.addAttribute("listeProfils", listeProfils) ;
		 
		 ResponseEntity<Employe[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		 List<Employe> listeEmployes = Arrays.asList(response2.getBody());
		 model.addAttribute("listeEmployes", listeEmployes) ;
	}
	
}
