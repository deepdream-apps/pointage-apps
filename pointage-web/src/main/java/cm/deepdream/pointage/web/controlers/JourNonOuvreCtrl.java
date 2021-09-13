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

import cm.deepdream.pointage.model.JourNonOuvre;
import cm.deepdream.pointage.model.Poste;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class JourNonOuvreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(JourNonOuvreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/joursnonouvres")
	public String index (Model model) throws Exception {
		 ResponseEntity<JourNonOuvre[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/journonouvre/all", JourNonOuvre[].class) ;
		 List<JourNonOuvre> listeJours = Arrays.asList(response.getBody());
		 model.addAttribute("listeJoursNonOuvres", listeJours) ;
		return "parametrage/joursnonouvres" ;
	}
	

	@GetMapping ("/parametrage/ajout-journonouvre")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		JourNonOuvre jourNonOuvre = new JourNonOuvre() ;
		model.addAttribute("jourNonOuvre", jourNonOuvre) ;
		return "parametrage/ajout-journonouvre" ;
	}
	
	@PostMapping ("/parametrage/journonouvre/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, JourNonOuvre jourNonOuvre) throws Exception {
		try {
			jourNonOuvre.setCreateur(utilisateurCourant.getLogin());
			jourNonOuvre.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/journonouvre/ajout", jourNonOuvre, JourNonOuvre.class);			
			return "redirect:/parametrage/joursnonouvres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("jourNonOuvre", jourNonOuvre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-journonouvre" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-journonouvre/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		JourNonOuvre jourNonOuvre = rest.getForObject("http://pointage-zuul/pointage-server/ws/journonouvre/id/{id}", 
				JourNonOuvre.class, id) ;
		model.addAttribute("jourNonOuvreExistant", jourNonOuvre) ;
		return "parametrage/maj-journonouvre" ;
	}
	
	@PostMapping ("/parametrage/journonouvre/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			JourNonOuvre jourNonOuvreExistant) throws Exception {
		try {
			JourNonOuvre jourNonOuvre = rest.getForObject("http://pointage-zuul/pointage-server/ws/journonouvre/id/{id}", 
					JourNonOuvre.class, jourNonOuvreExistant.getId()) ;
			jourNonOuvre.setDateDernMaj(LocalDateTime.now()) ;
			jourNonOuvre.setModificateur(utilisateurCourant.getLogin()) ;
			jourNonOuvre.setLibelle(jourNonOuvreExistant.getLibelle());
			jourNonOuvre.setJourMois(jourNonOuvreExistant.getJourMois());
			jourNonOuvre.setMois(jourNonOuvreExistant.getMois());
			jourNonOuvre.setAnnee(jourNonOuvreExistant.getAnnee());
			rest.put("http://pointage-zuul/pointage-server/ws/journonouvre/maj", jourNonOuvre, JourNonOuvre.class);
			return "redirect:/parametrage/joursnonouvres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("jourNonOuvreExistant", jourNonOuvreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-journonouvre" ;
		}
	}
	
	@GetMapping ("/parametrage/details-journonouvre/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		JourNonOuvre jourNonOuvre = rest.getForObject("http://pointage-zuul/pointage-server/ws/journonouvre/id/{id}", 
				JourNonOuvre.class, id) ;
		model.addAttribute("jourNonOuvreExistant", jourNonOuvre) ;
		return "parametrage/details-journonouvre" ;
	}
	
	
	@PostMapping ("/parametrage/journonouvre/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			JourNonOuvre jourNonOuvreExistant) throws Exception {
		try {
			JourNonOuvre jourNonOuvre = rest.getForObject("http://pointage-zuul/pointage-server/ws/journonouvre/id/{id}", 
					JourNonOuvre.class, jourNonOuvreExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", jourNonOuvre.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/journonouvre/suppr/{id}", uriVariables);
			return "redirect:/parametrage/joursnonouvres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("jourNonOuvreExistant", jourNonOuvreExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-journonouvre" ;
		}
	}
	
}
