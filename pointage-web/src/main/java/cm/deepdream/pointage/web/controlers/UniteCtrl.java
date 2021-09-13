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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class UniteCtrl implements Serializable{
	private Logger logger = Logger.getLogger(UniteCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/personnel/unites")
	public String index (Model model) throws Exception {
		 ResponseEntity<UniteAdministrative[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/unite/all", UniteAdministrative[].class) ;
		 List<UniteAdministrative> listeUnites = Arrays.asList(response.getBody());
		 model.addAttribute("listeUnites", listeUnites) ;
		return "personnel/unites" ;
	}
	

	@GetMapping ("/personnel/ajout-unite")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		UniteAdministrative unite = new UniteAdministrative() ;
		model.addAttribute("unite", unite) ;
		return "personnel/ajout-unite" ;
	}
	
	@PostMapping ("/personnel/unite/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, UniteAdministrative unite) throws Exception {
		try {
			unite.setCreateur(utilisateurCourant.getLogin());
			unite.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/unite/ajout", unite, UniteAdministrative.class);			
			return "redirect:/personnel/unites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("unite", unite) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/ajout-unite" ;
		}
	}
	
	@GetMapping ("/personnel/maj-unite/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		UniteAdministrative unite = rest.getForObject("http://pointage-zuul/pointage-server/ws/unite/id/{id}", 
				UniteAdministrative.class, id) ;
		model.addAttribute("uniteExistante", unite) ;
		return "personnel/maj-unite" ;
	}
	
	@PostMapping ("/personnel/unite/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			UniteAdministrative uniteExistante) throws Exception {
		try {
			UniteAdministrative unite = rest.getForObject("http://pointage-zuul/pointage-server/ws/unite/id/{id}", 
					UniteAdministrative.class, uniteExistante.getId()) ;
			unite.setDateDernMaj(LocalDateTime.now()) ;
			unite.setModificateur(utilisateurCourant.getLogin()) ;
			unite.setLibelle(uniteExistante.getLibelle());
			rest.put("http://pointage-zuul/pointage-server/ws/unite/maj", unite, UniteAdministrative.class);
			return "redirect:/personnel/unites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("uniteExistante", uniteExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/maj-unite" ;
		}
	}
	
	@GetMapping ("/personnel/details-unite/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		UniteAdministrative unite = rest.getForObject("http://pointage-zuul/pointage-server/ws/unite/id/{id}", UniteAdministrative.class, id) ;
		model.addAttribute("uniteExistante", unite) ;
		return "personnel/details-unite" ;
	}
	
	
	@PostMapping ("/personnel/unite/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			UniteAdministrative uniteExistante) throws Exception {
		try {
			UniteAdministrative unite = rest.getForObject("http://pointage-zuul/pointage-server/ws/unite/id/{id}", 
					UniteAdministrative.class, uniteExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", unite.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/unite/suppr/{id}", uriVariables);
			return "redirect:/personnel/unites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("uniteExistante", uniteExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/details-unite" ;
		}
	}
	
}
