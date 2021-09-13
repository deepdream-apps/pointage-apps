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

import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Prestation;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class PrestationCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PrestationCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/visite/prestations")
	public String index (Model model) throws Exception {
		 ResponseEntity<Prestation[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/prestation/all", Prestation[].class) ;
		 List<Prestation> listePrestations = Arrays.asList(response.getBody());
		 model.addAttribute("listePrestations", listePrestations) ;
		 return "visite/prestations" ;
	}
	
	@GetMapping ("/visite/prestationsoffertes")
	public String indexOffertes (Model model) throws Exception {
		 ResponseEntity<Prestation[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/prestation/offert", Prestation[].class) ;
		 List<Prestation> listePrestations = Arrays.asList(response.getBody());
		 model.addAttribute("listePrestations", listePrestations) ;
		 return "visite/prestationsoffertes" ;
	}
	
	@GetMapping ("/visite/prestationssouscrites")
	public String indexSouscrites (Model model) throws Exception {
		 ResponseEntity<Prestation[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/prestation/souscrit", Prestation[].class) ;
		 List<Prestation> listePrestations = Arrays.asList(response.getBody());
		 model.addAttribute("listePrestations", listePrestations) ;
		 return "visite/prestationssouscrites" ;
	}
	@GetMapping ("/visite/ajout-prestation")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		
		initDependencies (model) ;
		Prestation prestation = new Prestation() ;
		model.addAttribute("prestation", prestation) ;
		return "visite/ajout-prestation" ;
	}
	
	@PostMapping ("/visite/prestation/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Prestation prestation) throws Exception {
		try {
			prestation.setCreateur(utilisateurCourant.getLogin());
			prestation.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/prestation/ajout", prestation, Prestation.class);			
			return "redirect:/visite/prestations" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("prestation", prestation) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/ajout-prestation" ;
		}
	}
	
	@GetMapping ("/visite/maj-prestation/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Prestation prestation = rest.getForObject("http://pointage-zuul/pointage-server/ws/prestation/id/{id}", Prestation.class, id) ;
		initDependencies (model) ;
		model.addAttribute("prestationExistante", prestation) ;
		return "visite/maj-prestation" ;
	}
	
	@PostMapping ("/visite/prestation/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Prestation prestationExistante) throws Exception {
		try {
			Prestation prestation = rest.getForObject("http://pointage-zuul/pointage-server/ws/prestation/id/{id}", 
					Prestation.class, prestationExistante.getId()) ;
			prestation.setDateDernMaj(LocalDateTime.now()) ;
			prestation.setModificateur(utilisateurCourant.getLogin()) ;
			prestation.setDatePrest(prestationExistante.getDatePrest());
			prestation.setHeureDebut(prestationExistante.getHeureDebut());
			prestation.setHeureFin(prestationExistante.getHeureFin());
			prestation.setPointFocal1(prestationExistante.getPointFocal1());
			prestation.setPointFocal2(prestationExistante.getPointFocal2());
			prestation.setLibelle(prestationExistante.getLibelle());
			prestation.setType(prestationExistante.getType());
			rest.put("http://pointage-zuul/pointage-server/ws/prestation/maj", prestation, Prestation.class);
			return "redirect:/visite/prestations" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("prestationExistante", prestationExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/maj-prestation" ;
		}
	}
	
	@GetMapping ("/visite/details-prestation/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Prestation prestation = rest.getForObject("http://pointage-zuul/pointage-server/ws/prestation/id/{id}", 
				Prestation.class, id) ;
		model.addAttribute("prestationExistante", prestation) ;
		return "visite/details-prestation" ;
	}
	
	
	@PostMapping ("/visite/prestation/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Prestation prestationExistant) throws Exception {
		try {
			Prestation prestation = rest.getForObject("http://pointage-zuul/pointage-server/ws/prestation/id/{id}", 
					Prestation.class, prestationExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", prestation.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/prestation/suppr/{id}", uriVariables);
			return "redirect:/visite/prestations" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("prestationExistante", prestationExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/details-prestation" ;
		}
	}
	
	private void initDependencies (Model model) {
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ;
	}
	
}
