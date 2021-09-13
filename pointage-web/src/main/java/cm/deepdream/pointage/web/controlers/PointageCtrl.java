package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.model.Site;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.web.wrapper.DateJour;
import cm.deepdream.pointage.web.wrapper.Periode;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class PointageCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PointageCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	
	@GetMapping ("/pointage/pointages-aujourdhui")
	public String indexCeJour (Model model) throws Exception {
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/date/{date}", Pointage[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 model.addAttribute("listePointages", listePointages) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/pointages-aujourdhui" ;
	}
	
	
	@GetMapping ("/pointage/pointages-semainecourante")
	public String indexCetteSemaine (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1) ;
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/from/{from}/to/{to}", 
				 Pointage[].class, from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 model.addAttribute("listePointages", listePointages) ;
		 model.addAttribute("to", to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("from", from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/pointages-semainecourante" ;
	}
	
	@GetMapping ("/pointage/pointages-jour")
	public String indexHier (Model model) throws Exception {
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/date/{date}", Pointage[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 
		 DateJour dateJour = new DateJour() ;
		 dateJour.setDateJour(LocalDate.now()) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("listePointages", listePointages) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/pointages-jour" ;
	}
	
	@PostMapping ("/pointage/recherche-pointages-jour")
	public String rechercherJour (Model model, DateJour dateJour) throws Exception {
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/date/{date}", Pointage[].class, 
				 dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 model.addAttribute("listePointages", listePointages) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("date", dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/pointages-jour" ;
	}
	
	
	@GetMapping ("/pointage/pointages-periode")
	public String indexPeriode (Model model) throws Exception {
		 Periode periode = new Periode() ;
		 periode.setDateDebut(LocalDate.now()) ;
		 periode.setDateFin(LocalDate.now()) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/from/{from}/to/{to}", 
				 Pointage[].class, periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 model.addAttribute("listePointages", listePointages) ;
		 return "pointage/pointages-periode" ;
	}
	
	@PostMapping ("/pointage/recherche-pointages-periode")
	public String rechercherPeriode (Model model, Periode periode) throws Exception {
		 ResponseEntity<Pointage[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pointage/from/{from}/to/{to}", Pointage[].class, 
				 periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Pointage> listePointages = Arrays.asList(response.getBody());
		 model.addAttribute("listePointages", listePointages) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/pointages-periode" ;
	}
	

	@GetMapping ("/pointage/ajout-pointage")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		initDependencies (model) ;
		Pointage pointage = new Pointage() ;
		pointage.setDatePointage(LocalDate.now());
		pointage.setHeure(LocalTime.now());
		model.addAttribute("pointage", pointage) ;
		return "pointage/ajout-pointage" ;
	}
	
	@PostMapping ("/pointage/pointage/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Pointage pointage) throws Exception {
		try {
			pointage.setCreateur(utilisateurCourant.getLogin()) ;
			pointage.setModificateur(utilisateurCourant.getLogin()) ;
			pointage.setType("Manuel") ;
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/pointage/ajout", pointage, Pointage.class);			
			initDependencies (model) ;
			return "redirect:/pointage/pointages-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("pointage", pointage) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/ajout-pointage" ;
		}
	}
	
	@GetMapping ("/pointage/maj-pointage/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Pointage pointage = rest.getForObject("http://pointage-zuul/pointage-server/ws/pointage/id/{id}", Pointage.class, id) ;
		
		initDependencies (model) ;
		
		model.addAttribute("pointageExistant", pointage) ;
		return "pointage/maj-pointage" ;
	}
	
	@PostMapping ("/pointage/pointage/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Pointage pointageExistant) throws Exception {
		try {
			Pointage pointage = rest.getForObject("http://pointage-zuul/pointage-server/ws/pointage/id/{id}", Pointage.class, pointageExistant.getId()) ;
			pointage.setDateDernMaj(LocalDateTime.now()) ;
			pointage.setModificateur(utilisateurCourant.getLogin()) ;
			pointage.setEmploye(pointageExistant.getEmploye()) ;
			pointage.setDatePointage(pointageExistant.getDatePointage()) ;
			pointage.setHeureRequise(pointageExistant.getHeureRequise()) ;
			pointage.setHeure(pointageExistant.getHeure()) ;
			pointage.setDirection(pointageExistant.getDirection()) ;
			pointage.setType(pointageExistant.getType() == null ? "Manuel":pointageExistant.getType()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/pointage/maj", pointage, Pointage.class);
			return "redirect:/pointage/pointages-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("pointageExistant", pointageExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/maj-pointage" ;
		}
	}
	
	@GetMapping ("/pointage/details-pointage/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Pointage pointage = rest.getForObject("http://pointage-zuul/pointage-server/ws/pointage/id/{id}", 
				Pointage.class, id) ;
		model.addAttribute("pointageExistant", pointage) ;
		return "pointage/details-pointage" ;
	}
	
	
	@PostMapping ("/pointage/pointage/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Pointage pointageExistant) throws Exception {
		try {
			Pointage pointage = rest.getForObject("http://pointage-zuul/pointage-server/ws/pointage/id/{id}", 
					Pointage.class, pointageExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", pointage.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/pointage/suppr/{id}", uriVariables);
			return "redirect:/pointage/pointages" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("pointageExistant", pointageExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/details-pointage" ;
		}
	}
	
	private void initDependencies (Model model) {
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ; 
		
		ResponseEntity<Site[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/site/all", Site[].class) ;
		List<Site> listeSites = Arrays.asList(response2.getBody());
		model.addAttribute("listeSites", listeSites) ; 
	}
	
}
