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
import cm.deepdream.pointage.model.Visiteur;
import cm.deepdream.pointage.web.wrapper.DateJour;
import cm.deepdream.pointage.web.wrapper.Periode;
import cm.deepdream.pointage.model.Pays;
import cm.deepdream.pointage.model.Visiteur;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class VisiteurCtrl implements Serializable{
	private Logger logger = Logger.getLogger(VisiteurCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;

	
	@GetMapping ("/visite/visiteursrecents")
	public String visiteursRecents (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(30) ;
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/from/{from}/to/{to}", 
				 Visiteur[].class, from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 model.addAttribute("to", to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("from", from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visite/visiteursrecents" ;
	}
	
	@GetMapping ("/visite/visiteursfrequents")
	public String visiteursFrequents (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(30) ;
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/frequents/{nombre}", 
				 Visiteur[].class, 100) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 model.addAttribute("nombre", 100) ;
		 return "visite/visiteursrecents" ;
	}
	
	
	@GetMapping ("/visiteur/visiteurs-jour")
	public String indexHier (Model model) throws Exception {
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/date/{date}", Visiteur[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 
		 DateJour dateJour = new DateJour() ;
		 dateJour.setDateJour(LocalDate.now()) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visiteur/visiteurs-jour" ;
	}
	
	@PostMapping ("/visiteur/recherche-visiteurs-jour")
	public String rechercherJour (Model model, DateJour dateJour) throws Exception {
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/date/{date}", Visiteur[].class, 
				 dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("date", dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visiteur/visiteurs-jour" ;
	}
	
	
	@GetMapping ("/visiteur/visiteurs-periode")
	public String indexPeriode (Model model) throws Exception {
		 Periode periode = new Periode() ;
		 periode.setDateDebut(LocalDate.now()) ;
		 periode.setDateFin(LocalDate.now()) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/from/{from}/to/{to}", 
				 Visiteur[].class, periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 return "visiteur/visiteurs-periode" ;
	}
	
	
	@PostMapping ("/visiteur/recherche-visiteurs-periode")
	public String rechercherPeriode (Model model, Periode periode) throws Exception {
		 ResponseEntity<Visiteur[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visiteur/from/{from}/to/{to}", Visiteur[].class, 
				 periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Visiteur> listeVisiteurs = Arrays.asList(response.getBody());
		 model.addAttribute("listeVisiteurs", listeVisiteurs) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "visiteur/visiteurs-periode" ;
	}
	
	
	@GetMapping ("/visiteur/ajout-visiteur")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		initDependencies(model) ;		
		Visiteur visiteur = new Visiteur() ;
		model.addAttribute("visiteur", visiteur) ;
		return "visiteur/ajout-visiteur" ;
	}
	
	@PostMapping ("/visiteur/visiteur/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Visiteur visiteur) throws Exception {
		try {
			visiteur.setCreateur(utilisateurCourant.getLogin());
			visiteur.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/visiteur/ajout", visiteur, Visiteur.class);			
			return "redirect:/visiteur/visiteurs-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;	
			model.addAttribute("visiteur", visiteur) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visiteur/ajout-visiteur" ;
		}
	}
	
	@GetMapping ("/visiteur/maj-visiteur/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Visiteur visiteur = rest.getForObject("http://pointage-zuul/pointage-server/ws/visiteur/id/{id}", Visiteur.class, id) ;
		model.addAttribute("visiteurExistante", visiteur) ;
		initDependencies(model) ;	
		return "visiteur/maj-visiteur" ;
	}
	
	@PostMapping ("/visiteur/visiteur/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Visiteur visiteurExistante) throws Exception {
		try {
			Visiteur visiteur = rest.getForObject("http://pointage-zuul/pointage-server/ws/visiteur/id/{id}", 
					Visiteur.class, visiteurExistante.getId()) ;
			visiteur.setDateDernMaj(LocalDateTime.now()) ;
			visiteur.setModificateur(utilisateurCourant.getLogin()) ;
			visiteur.setTypeId(visiteurExistante.getTypeId()) ;
			visiteur.setNumeroId(visiteurExistante.getNumeroId()) ;
			visiteur.setPhoto(visiteurExistante.getPhoto()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/visiteur/maj", visiteur, Visiteur.class);
			return "redirect:/visiteur/visiteurs-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model) ;	
			model.addAttribute("visiteurExistante", visiteurExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visiteur/maj-visiteur" ;
		}
	}
	
	@GetMapping ("/visiteur/details-visiteur/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Visiteur visiteur = rest.getForObject("http://pointage-zuul/pointage-server/ws/visiteur/id/{id}", 
				Visiteur.class, id) ;
		model.addAttribute("visiteurExistante", visiteur) ;
		return "visiteur/details-visiteur" ;
	}
	
	
	@PostMapping ("/visiteur/visiteur/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Visiteur visiteurExistante) throws Exception {
		try {
			Visiteur visiteur = rest.getForObject("http://pointage-zuul/pointage-server/ws/visiteur/id/{id}", 
					Visiteur.class, visiteurExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", visiteur.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/visiteur/suppr/{id}", uriVariables);
			return "redirect:/visiteur/visiteurs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			
			model.addAttribute("visiteurExistant", visiteurExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "visiteur/details-visiteur" ;
		}
	}
	
	private void initDependencies(Model model) {
		ResponseEntity<Pays[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pays/all", Pays[].class) ;
		List<Pays> listePays = Arrays.asList(response2.getBody());
		model.addAttribute("listePays", listePays) ;
	}
	
}
