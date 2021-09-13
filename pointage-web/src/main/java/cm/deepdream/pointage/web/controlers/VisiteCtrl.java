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
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.web.wrapper.DateJour;
import cm.deepdream.pointage.web.wrapper.Periode;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Evenement;
import cm.deepdream.pointage.model.Pays;
import cm.deepdream.pointage.model.Prestation;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class VisiteCtrl implements Serializable{
	private Logger logger = Logger.getLogger(VisiteCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/visite/visites-aujourdhui")
	public String index (Model model) throws Exception {
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/date/{date}", 
				 Visite[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisites", listeVisites) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visites-aujourdhui" ;
	}
	
	@GetMapping ("/visite/visites-semainecourante")
	public String indexCetteSemaine (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1) ;
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/from/{from}/to/{to}", 
				 Visite[].class, from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisites", listeVisites) ;
		 model.addAttribute("to", to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("from", from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visites-semainecourante" ;
	}
	
	
	@GetMapping ("/visite/visites-jour")
	public String indexHier (Model model) throws Exception {
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/date/{date}", Visite[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 
		 DateJour dateJour = new DateJour() ;
		 dateJour.setDateJour(LocalDate.now()) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("listeVisites", listeVisites) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visites-jour" ;
	}
	
	@PostMapping ("/visite/recherche-visites-jour")
	public String rechercherJour (Model model, DateJour dateJour) throws Exception {
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/date/{date}", Visite[].class, 
				 dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisites", listeVisites) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("date", dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visites-jour" ;
	}
	
	
	@GetMapping ("/visite/visites-periode")
	public String indexPeriode (Model model) throws Exception {
		 Periode periode = new Periode() ;
		 periode.setDateDebut(LocalDate.now()) ;
		 periode.setDateFin(LocalDate.now()) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/from/{from}/to/{to}", 
				 Visite[].class, periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisites", listeVisites) ;
		 return "visite/visites-periode" ;
	}
	
	
	@PostMapping ("/visite/recherche-visites-periode")
	public String rechercherPeriode (Model model, Periode periode) throws Exception {
		 ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/from/{from}/to/{to}", Visite[].class, 
				 periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visite> listeVisites = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisites", listeVisites) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visites-periode" ;
	}
	
	
	@GetMapping ("/visite/ajout-visite")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		initDependencies(model) ;		
		Visite visite = new Visite() ;
		visite.setDateVisite(LocalDate.now());
		visite.setHeureArrivee(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()));
		visite.setMotif(1);
		model.addAttribute("visite", visite) ;
		return "visite/ajout-visite" ;
	}
	
	@PostMapping ("/visite/visite/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Visite visite) throws Exception {
		try {
			visite.setCreateur(utilisateurCourant.getLogin());
			visite.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/visite/ajout", visite, Visite.class);			
			return "redirect:/visite/visites-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;	
			model.addAttribute("visite", visite) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/ajout-visite" ;
		}
	}
	
	@GetMapping ("/visite/maj-visite/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Visite visite = rest.getForObject("http://pointage-zuul/pointage-server/ws/visite/id/{id}", Visite.class, id) ;
		model.addAttribute("visiteExistante", visite) ;
		initDependencies(model) ;	
		return "visite/maj-visite" ;
	}
	
	@PostMapping ("/visite/visite/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Visite visiteExistante) throws Exception {
		try {
			Visite visite = rest.getForObject("http://pointage-zuul/pointage-server/ws/visite/id/{id}", 
					Visite.class, visiteExistante.getId()) ;
			visite.setDateDernMaj(LocalDateTime.now()) ;
			visite.setModificateur(utilisateurCourant.getLogin()) ;
			visite.setTypeId(visiteExistante.getTypeId()) ;
			visite.setNumeroId(visiteExistante.getNumeroId()) ;
			visite.setQualite(visiteExistante.getQualite()) ;
			visite.setNom(visiteExistante.getNom()) ;
			visite.setMotif(visiteExistante.getMotif()) ;
			visite.setEmploye(visiteExistante.getEmploye()) ;
			visite.setEvenement(visiteExistante.getEvenement());
			visite.setDateVisite(visiteExistante.getDateVisite()) ;
			visite.setHeureArrivee(visiteExistante.getHeureArrivee()) ;
			visite.setHeureDepart(visiteExistante.getHeureDepart()) ;
			visite.setMotif(visiteExistante.getMotif()) ;
			visite.setPhoto(visiteExistante.getPhoto()) ;
			visite.setCommentaires(visiteExistante.getCommentaires()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/visite/maj", visite, Visite.class);
			return "redirect:/visite/visites-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;	
			model.addAttribute("visiteExistante", visiteExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/maj-visite" ;
		}
	}
	
	@GetMapping ("/visite/details-visite/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Visite visite = rest.getForObject("http://pointage-zuul/pointage-server/ws/visite/id/{id}", 
				Visite.class, id) ;
		model.addAttribute("visiteExistante", visite) ;
		return "visite/details-visite" ;
	}
	
	
	@PostMapping ("/visite/visite/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Visite visiteExistante) throws Exception {
		try {
			Visite visite = rest.getForObject("http://pointage-zuul/pointage-server/ws/visite/id/{id}", 
					Visite.class, visiteExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", visite.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/visite/suppr/{id}", uriVariables);
			return "redirect:/visite/visites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			
			model.addAttribute("visiteExistante", visiteExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visite/details-visite" ;
		}
	}
	
	private void initDependencies(Model model) {
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ;
		
		ResponseEntity<Pays[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pays/all", Pays[].class) ;
		List<Pays> listePays = Arrays.asList(response2.getBody());
		model.addAttribute("listePays", listePays) ;
		
		LocalDate to = LocalDate.now() ;
		LocalDate from = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1) ;
		ResponseEntity<Evenement[]> response3 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/evenement/from/{from}/to/{to}", Evenement[].class,
				from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		List<Evenement> listeEvenements = Arrays.asList(response3.getBody());
		model.addAttribute("listeEvenements", listeEvenements) ;
		
		ResponseEntity<Prestation[]> response4 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/prestation/souscrit", Prestation[].class) ;
		List<Prestation> listePrestations = Arrays.asList(response4.getBody());
		model.addAttribute("listePrestations", listePrestations) ;
	}
	
}
