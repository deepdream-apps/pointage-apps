package cm.deepdream.pointage.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.model.Session;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class SessionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(SessionCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@GetMapping ("/admin/sessions")
	public String index (Model model) throws Exception {
		ResponseEntity<Session[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/session/all", Session[].class) ;
		List<Session> listeSessions = Arrays.asList(response.getBody());
		model.addAttribute("listeSessions", listeSessions) ;
		return "admin/sessions" ;
	}
	
	@GetMapping ("/admin/sessions/all")
	public String tous (Model model) throws Exception {
		return "admin/sessions" ;
	}
	
	@GetMapping ("/admin/sessions/profil/{id}")
	public String parProfil (Model model) throws Exception {
		return "admin/sessions" ;
	}
	
	@GetMapping ("/admin/ajout-session")
	public String initAjout (Model model) throws Exception {
		return "admin/ajout-session" ;
	}
	
	@PostMapping ("/admin/session/ajout")
	public String ajouter (Model model) throws Exception {
		return "admin/sessions" ;
	}
	
	 @GetMapping ("/admin/historique-connexions")
	 public String historiqueConnexions (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) {
		 ResponseEntity<Session[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/session/ecole/%d/utilisateur/%d", Session[].class) ;
		 List<Session> listeSessions = Arrays.asList(response.getBody());
		 model.addAttribute("listeSessions", listeSessions) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
	     return "admin/historique-connexions" ;
	 }

}
