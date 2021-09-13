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
import cm.deepdream.pointage.model.Retard;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.web.wrapper.DateJour;
import cm.deepdream.pointage.web.wrapper.Periode;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class RetardCtrl implements Serializable{
	private Logger logger = Logger.getLogger(RetardCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/pointage/retards-aujourdhui")
	public String index (Model model) throws Exception {
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/date/{date}", Retard[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 model.addAttribute("listeRetards", listeRetards) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/retards-aujourdhui" ;
	}
	
	
	@GetMapping ("/pointage/retards-semainecourante")
	public String indexCetteSemaine (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1) ;
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/from/{from}/to/{to}", 
				 Retard[].class, from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 model.addAttribute("listeRetards", listeRetards) ;
		 model.addAttribute("to", to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("from", from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/retards-semainecourante" ;
	}
	
	
	@GetMapping ("/pointage/retards-jour")
	public String indexHier (Model model) throws Exception {
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/date/{date}", Retard[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 
		 DateJour dateJour = new DateJour() ;
		 dateJour.setDateJour(LocalDate.now()) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("listeRetards", listeRetards) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/retards-jour" ;
	}
	
	
	@PostMapping ("/pointage/recherche-retards-jour")
	public String rechercherJour (Model model, DateJour dateJour) throws Exception {
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/date/{date}", Retard[].class, 
				 dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 model.addAttribute("listeRetards", listeRetards) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("date", dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/retards-jour" ;
	}
	
	
	@GetMapping ("/pointage/retards-periode")
	public String indexPeriode (Model model) throws Exception {
		 Periode periode = new Periode() ;
		 periode.setDateDebut(LocalDate.now()) ;
		 periode.setDateFin(LocalDate.now()) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/from/{from}/to/{to}", 
				 Retard[].class, periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 model.addAttribute("listeRetards", listeRetards) ;
		 return "pointage/retards-periode" ;
	}
	
	@PostMapping ("/pointage/recherche-retards-periode")
	public String rechercherPeriode (Model model, Periode periode) throws Exception {
		 ResponseEntity<Retard[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/retard/from/{from}/to/{to}", Retard[].class, 
				 periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Retard> listeRetards = Arrays.asList(response.getBody());
		 model.addAttribute("listeRetards", listeRetards) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/retards-periode" ;
	}
	
	
	@GetMapping ("/pointage/ajout-retard")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ;
		 
		Retard retard = new Retard() ;
		retard.setDateRetard(LocalDate.now());
		retard.setHeureArrivee(LocalTime.now());
		model.addAttribute("retard", retard) ;
		return "pointage/ajout-retard" ;
	}
	
	@PostMapping ("/pointage/retard/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Retard retard) throws Exception {
		try {
			retard.setCreateur(utilisateurCourant.getLogin()) ;
			retard.setModificateur(utilisateurCourant.getLogin()) ;
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/retard/ajout", retard, Retard.class);			
			return "redirect:/pointage/retards" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("retard", retard) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/ajout-retard" ;
		}
	}
	
	@GetMapping ("/pointage/maj-retard/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Retard retard = rest.getForObject("http://pointage-zuul/pointage-server/ws/retard/id/{id}", Retard.class, id) ;
		
		ResponseEntity<Employe[]> response = rest.getForEntity("http://retard-zuul/retard-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ;
		
		model.addAttribute("retardExistant", retard) ;
		return "pointage/maj-retard" ;
	}
	
	@PostMapping ("/pointage/retard/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Retard retardExistant) throws Exception {
		try {
			Retard retard = rest.getForObject("http://pointage-zuul/pointage-server/ws/retard/id/{id}", Retard.class, retardExistant.getId()) ;
			retard.setDateDernMaj(LocalDateTime.now()) ;
			retard.setModificateur(utilisateurCourant.getLogin()) ;
			retard.setEmploye(retardExistant.getEmploye()) ;
			retard.setDateRetard(retardExistant.getDateRetard()) ;
			retard.setDureeRetard(retardExistant.getDureeRetard()) ;
			retard.setHeureArrivee(retardExistant.getHeureArrivee()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/retard/maj", retard, Retard.class);
			return "redirect:/pointage/retards" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("retardExistant", retardExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/maj-retard" ;
		}
	}
	
	@GetMapping ("/pointage/details-retard/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Retard retard = rest.getForObject("http://pointage-zuul/pointage-server/ws/retard/id/{id}", 
				Retard.class, id) ;
		model.addAttribute("retardExistant", retard) ;
		return "pointage/details-retard" ;
	}
	
	
	@PostMapping ("/pointage/retard/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Retard retardExistant) throws Exception {
		try {
			Retard retard = rest.getForObject("http://pointage-zuul/pointage-server/ws/retard/id/{id}", 
					Retard.class, retardExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", retard.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/retard/suppr/{id}", uriVariables);
			return "redirect:/pointage/retards" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("retardExistant", retardExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/details-retard" ;
		}
	}
	
}
