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
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class StatutCtrl implements Serializable{
	private Logger logger = Logger.getLogger(StatutCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/personnel/statuts")
	public String index (Model model) throws Exception {
		 ResponseEntity<Statut[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/statut/all", Statut[].class) ;
		 List<Statut> listeStatuts = Arrays.asList(response.getBody());
		 model.addAttribute("listeStatuts", listeStatuts) ;
		 return "personnel/statuts" ;
	}
	

	@GetMapping ("/personnel/ajout-statut")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Statut statut = new Statut() ;
		model.addAttribute("statut", statut) ;
		return "personnel/ajout-statut" ;
	}
	
	@PostMapping ("/personnel/statut/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Statut statut) throws Exception {
		try {
			statut.setCreateur(utilisateurCourant.getLogin());
			statut.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/statut/ajout", statut, Statut.class);			
			return "redirect:/personnel/statuts" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("statut", statut) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/ajout-statut" ;
		}
	}
	
	@GetMapping ("/personnel/maj-statut/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Statut statut = rest.getForObject("http://pointage-zuul/pointage-server/ws/statut/id/{id}", Statut.class, id) ;

		model.addAttribute("statutExistant", statut) ;
		return "personnel/maj-statut" ;
	}
	
	@PostMapping ("/personnel/statut/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Statut statutExistant) throws Exception {
		try {
			Statut statut = rest.getForObject("http://pointage-zuul/pointage-server/ws/statut/id/{id}", 
					Statut.class, statutExistant.getId()) ;
			statut.setDateDernMaj(LocalDateTime.now()) ;
			statut.setModificateur(utilisateurCourant.getLogin()) ;
			statut.setLibelle(statutExistant.getLibelle()) ;
			statut.setDescription(statutExistant.getDescription());
			rest.put("http://pointage-zuul/pointage-server/ws/statut/maj", statut, Statut.class);
			return "redirect:/personnel/statuts" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("statutExistant", statutExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/maj-statut" ;
		}
	}
	
	@GetMapping ("/personnel/details-statut/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Statut statut = rest.getForObject("http://pointage-zuul/pointage-server/ws/statut/id/{id}", 
			Statut.class, id) ;
		model.addAttribute("statutExistant", statut) ;
		return "personnel/details-statut" ;
	}
	
	
	@PostMapping ("/personnel/statut/suppr")
	public String supprimer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Statut statutExistant) throws Exception {
		try {
			Statut statut = rest.getForObject("http://pointage-zuul/pointage-server/ws/statut/id/{id}", Statut.class, statutExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", statut.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/statut/suppr/{id}", uriVariables);
			return "redirect:/personnel/statuts" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("statutExistant", statutExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/details-statut" ;
		}
	}
	
}
