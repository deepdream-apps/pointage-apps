package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import cm.deepdream.pointage.model.Horaire;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class HoraireCtrl implements Serializable{
	private Logger logger = Logger.getLogger(HoraireCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/horaires")
	public String index (Model model) throws Exception {
		 ResponseEntity<Horaire[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/horaire/all", Horaire[].class) ;
		 List<Horaire> listeHoraires = Arrays.asList(response.getBody());
		 model.addAttribute("listeHoraires", listeHoraires) ;
		return "parametrage/horaires" ;
	}
	

	@GetMapping ("/parametrage/ajout-horaire")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		initDependencies (model) ;
		Horaire horaire = new Horaire() ;
		horaire.setHeureArrivee(LocalTime.of(7, 30)) ;
		horaire.setHeureDepart(LocalTime.of(15, 30)) ;
		model.addAttribute("horaire", horaire) ;
		return "parametrage/ajout-horaire" ;
	}
	
	@PostMapping ("/parametrage/horaire/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Horaire horaire) throws Exception {
		try {
			horaire.setCreateur(utilisateurCourant.getLogin());
			horaire.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/horaire/ajout", horaire, Horaire.class);			
			return "redirect:/parametrage/horaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("horaire", horaire) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-horaire" ;
		}
	}
	
	@GetMapping ("/parametrage/maj-horaire/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		initDependencies (model) ;
		Horaire horaire = rest.getForObject("http://pointage-zuul/pointage-server/ws/horaire/id/{id}", 
				Horaire.class, id) ;
		model.addAttribute("horaireExistant", horaire) ;
		return "parametrage/maj-horaire" ;
	}
	
	@PostMapping ("/parametrage/horaire/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Horaire horaireExistant) throws Exception {
		try {
			Horaire horaire = rest.getForObject("http://pointage-zuul/pointage-server/ws/horaire/id/{id}", Horaire.class, horaireExistant.getId()) ;
			horaire.setDateDernMaj(LocalDateTime.now()) ;
			horaire.setModificateur(utilisateurCourant.getLogin()) ;
			horaire.setHeureArrivee(horaireExistant.getHeureArrivee());
			horaire.setHeureDepart(horaireExistant.getHeureDepart());
			horaire.setStatut(horaireExistant.getStatut());
			rest.put("http://pointage-zuul/pointage-server/ws/horaire/maj", horaire, Horaire.class);
			return "redirect:/parametrage/horaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("horaireExistant", horaireExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-horaire" ;
		}
	}
	
	@GetMapping ("/parametrage/details-horaire/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Horaire horaire = rest.getForObject("http://pointage-zuul/pointage-server/ws/horaire/id/{id}", Horaire.class, id) ;
		model.addAttribute("horaireExistant", horaire) ;
		return "parametrage/details-horaire" ;
	}
	
	
	@PostMapping ("/parametrage/horaire/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Horaire horaireExistant) throws Exception {
		try {
			Horaire horaire = rest.getForObject("http://pointage-zuul/pointage-server/ws/horaire/id/{id}", 
					Horaire.class, horaireExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", horaire.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/horaire/suppr/{id}", uriVariables);
			return "redirect:/parametrage/horaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("horaireExistant", horaireExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-horaire" ;
		}
	}
	
	
	private void initDependencies (Model model) {
		 ResponseEntity<Statut[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/statut/all", Statut[].class) ;
		 List<Statut> listeStatuts = Arrays.asList(response.getBody());
		 model.addAttribute("listeStatuts", listeStatuts) ;
	}
	
}
