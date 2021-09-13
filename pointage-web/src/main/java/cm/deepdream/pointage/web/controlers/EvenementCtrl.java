package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import cm.deepdream.pointage.model.Evenement;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class EvenementCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EvenementCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/visite/evenements-aujourdhui")
	public String index (Model model) throws Exception {
		 ResponseEntity<Evenement[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/evenement/date/{date}", 
				 Evenement[].class, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Evenement> listeEvenements = Arrays.asList(response.getBody());
		 model.addAttribute("listeEvenements", listeEvenements) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/evenements-aujourdhui" ;
	}
	
	
	@GetMapping ("/visite/evenements-anneecourante")
	public String indexAnnee (Model model) throws Exception {
		 ResponseEntity<Evenement[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/evenement/all", Evenement[].class) ;
		 List<Evenement> listeEvenements = Arrays.asList(response.getBody());
		 model.addAttribute("listeEvenements", listeEvenements) ;
		return "visite/evenements" ;
	}
	

	@GetMapping ("/visite/ajout-evenement")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Evenement evenement = new Evenement() ;
		evenement.setDateEvnmt(LocalDate.now()) ;
		evenement.setHeureDebut(LocalTime.now());
		model.addAttribute("evenement", evenement) ;
		initDependencies(model);
		return "visite/ajout-evenement" ;
	}
	
	@PostMapping ("/visite/evenement/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Evenement evenement) throws Exception {
		try {
			evenement.setCreateur(utilisateurCourant.getLogin());
			evenement.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/evenement/ajout", evenement, Evenement.class);			
			return "redirect:/visite/evenements-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model);
			model.addAttribute("evenement", evenement) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/ajout-evenement" ;
		}
	}
	
	@GetMapping ("/visite/maj-evenement/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Evenement evenement = rest.getForObject("http://pointage-zuul/pointage-server/ws/evenement/id/{id}", Evenement.class, id) ;
		model.addAttribute("evenementExistant", evenement) ;
		initDependencies(model);
		return "visite/maj-evenement" ;
	}
	
	
	@PostMapping ("/visite/evenement/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Evenement evenementExistant) throws Exception {
		try {
			Evenement evenement = rest.getForObject("http://pointage-zuul/pointage-server/ws/evenement/id/{id}", Evenement.class, evenementExistant.getId()) ;
			evenement.setDateDernMaj(LocalDateTime.now()) ;
			evenement.setModificateur(utilisateurCourant.getLogin()) ;
			evenement.setLibelle(evenementExistant.getLibelle()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/evenement/maj", evenement, Evenement.class);
			return "redirect:/visite/evenements" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model);
			model.addAttribute("evenementExistant", evenementExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/maj-evenement" ;
		}
	}
	
	@GetMapping ("/visite/details-evenement/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Evenement evenement = rest.getForObject("http://pointage-zuul/pointage-server/ws/evenement/id/{id}", Evenement.class, id) ;
		model.addAttribute("evenementExistant", evenement) ;
		return "visite/details-evenement" ;
	}
	
	
	@PostMapping ("/visite/evenement/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Evenement evenementExistant) throws Exception {
		try {
			Evenement evenement = rest.getForObject("http://pointage-zuul/pointage-server/ws/evenement/id/{id}", Evenement.class, evenementExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", evenement.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/evenement/suppr/{id}", uriVariables);
			return "redirect:/visite/evenements" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("evenementExistant", evenementExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/details-evenement" ;
		}
	}
	
	private void initDependencies (Model model) {
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ;
	}
	
}
