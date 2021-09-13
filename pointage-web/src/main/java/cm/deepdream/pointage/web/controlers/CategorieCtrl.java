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
import cm.deepdream.pointage.model.Categorie;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class CategorieCtrl implements Serializable{
	private Logger logger = Logger.getLogger(CategorieCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/categories")
	public String index (Model model) throws Exception {
		 ResponseEntity<Categorie[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/categorie/all", Categorie[].class) ;
		 List<Categorie> listeCategories = Arrays.asList(response.getBody());
		 model.addAttribute("listeCategories", listeCategories) ;
		return "parametrage/categories" ;
	}
	

	@GetMapping ("/parametrage/ajout-categorie")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		
		ResponseEntity<Rang[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/rang/all", Rang[].class) ;
		List<Rang> listeRangs = Arrays.asList(response.getBody());
		model.addAttribute("listeRangs", listeRangs) ;
		
		ResponseEntity<UniteAdministrative[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/unite/all", UniteAdministrative[].class) ;
		List<UniteAdministrative> listeUnites = Arrays.asList(response2.getBody());
		model.addAttribute("listeUnites", listeUnites) ;
		 
		Categorie categorie = new Categorie() ;
		model.addAttribute("categorie", categorie) ;
		return "parametrage/ajout-categorie" ;
	}
	
	@PostMapping ("/parametrage/categorie/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Categorie categorie) throws Exception {
		try {
			categorie.setCreateur(utilisateurCourant.getLogin());
			categorie.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/categorie/ajout", categorie, Categorie.class);			
			return "redirect:/parametrage/categories" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("categorie", categorie) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-categorie" ;
		}
	}
	
	@GetMapping ("/parametrage/maj-categorie/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Categorie categorie = rest.getForObject("http://pointage-zuul/pointage-server/ws/categorie/id/{id}", Categorie.class, id) ;
		
		ResponseEntity<Rang[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/rang/all", Rang[].class) ;
		List<Rang> listeRangs = Arrays.asList(response.getBody());
		model.addAttribute("listeRangs", listeRangs) ;
		
		ResponseEntity<UniteAdministrative[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/unite/all", UniteAdministrative[].class) ;
		List<UniteAdministrative> listeUnites = Arrays.asList(response2.getBody());
		model.addAttribute("listeUnites", listeUnites) ;
		
		model.addAttribute("categorieExistante", categorie) ;
		return "parametrage/maj-categorie" ;
	}
	
	@PostMapping ("/parametrage/categorie/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Categorie categorieExistant) throws Exception {
		try {
			Categorie categorie = rest.getForObject("http://pointage-zuul/pointage-server/ws/categorie/id/{id}", 
					Categorie.class, categorieExistant.getId()) ;
			categorie.setDateDernMaj(LocalDateTime.now()) ;
			categorie.setModificateur(utilisateurCourant.getLogin()) ;
			categorie.setLibelle(categorieExistant.getLibelle()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/categorie/maj", categorie, Categorie.class);
			return "redirect:/parametrage/categories" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("categorieExistant", categorieExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-categorie" ;
		}
	}
	
	@GetMapping ("/parametrage/details-categorie/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Categorie categorie = rest.getForObject("http://pointage-zuul/pointage-server/ws/categorie/id/{id}", 
				Categorie.class, id) ;
		model.addAttribute("categorieExistante", categorie) ;
		return "parametrage/details-categorie" ;
	}
	
	
	@PostMapping ("/parametrage/categorie/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Categorie categorieExistant) throws Exception {
		try {
			Categorie categorie = rest.getForObject("http://pointage-zuul/pointage-server/ws/categorie/id/{id}", 
					Categorie.class, categorieExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", categorie.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/categorie/suppr/{id}", uriVariables);
			return "redirect:/parametrage/categories" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("categorieExistant", categorieExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-categorie" ;
		}
	}
	
}
