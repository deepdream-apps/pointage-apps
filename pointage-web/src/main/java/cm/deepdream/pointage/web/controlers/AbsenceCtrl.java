package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import cm.deepdream.pointage.enums.Booleen;
import cm.deepdream.pointage.model.Absence;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.web.wrapper.DateJour;
import cm.deepdream.pointage.web.wrapper.Periode;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class AbsenceCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AbsenceCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/pointage/absences-aujourdhui")
	public String indexCeJour (Model model) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/date/{date}", Absence[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/absences-aujourdhui" ;
	}
	
	
	@GetMapping ("/pointage/absences-semainecourante")
	public String indexCetteSemaine (Model model) throws Exception {
		 LocalDate to = LocalDate.now() ;
		 LocalDate from = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1) ;
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/from/{from}/to/{to}", 
				 Absence[].class, from.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), to.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 model.addAttribute("to", to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("from", from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/absences-semainecourante" ;
	}
	
	@GetMapping ("/pointage/absences-jour")
	public String indexHier (Model model) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/date/{date}", Absence[].class, 
				 LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 
		 DateJour dateJour = new DateJour() ;
		 dateJour.setDateJour(LocalDate.now()) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 model.addAttribute("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/absences-jour" ;
	}
	
	@PostMapping ("/pointage/recherche-absences-jour")
	public String rechercherJour (Model model, DateJour dateJour) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/date/{date}", Absence[].class, 
				 dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 model.addAttribute("dateJour", dateJour) ;
		 model.addAttribute("date", dateJour.getDateJour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/absences-jour" ;
	}
	
	
	@GetMapping ("/pointage/absences-periode")
	public String indexPeriode (Model model) throws Exception {
		 Periode periode = new Periode() ;
		 periode.setDateDebut(LocalDate.now()) ;
		 periode.setDateFin(LocalDate.now()) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/from/{from}/to/{to}", 
				 Absence[].class, periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 return "pointage/absences-periode" ;
	}
	
	@PostMapping ("/pointage/recherche-absences-periode")
	public String rechercherPeriode (Model model, Periode periode) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/absence/from/{from}/to/{to}", Absence[].class, 
				 periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				 periode.getDateFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 model.addAttribute("periode", periode) ;
		 model.addAttribute("from", periode.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 model.addAttribute("to", periode.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) ;
		 return "pointage/absences-periode" ;
	}
	

	@GetMapping ("/pointage/ajout-absence")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Absence absence = new Absence() ;
		model.addAttribute("absence", absence) ;
		return "pointage/ajout-absence" ;
	}
	
	@PostMapping ("/pointage/absence/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Absence absence) throws Exception {
		try {
			absence.setCreateur(utilisateurCourant.getLogin());
			absence.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/absence/ajout", absence, Absence.class);			
			return "redirect:/pointage/absences-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absence", absence) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/ajout-absence" ;
		}
	}
	
	
	@GetMapping ("/pointage/maj-absence/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Absence absence = rest.getForObject("http://pointage-zuul/pointage-server/ws/absence/id/{id}", Absence.class, id) ;
		model.addAttribute("absenceExistante", absence) ;
		initDependencies (model) ;
		return "pointage/maj-absence" ;
	}
	
	@PostMapping ("/pointage/absence/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Absence absenceExistante) throws Exception {
		try {
			Absence absence = rest.getForObject("http://pointage-zuul/pointage-server/ws/absence/id/{id}", 
					Absence.class, absenceExistante.getId()) ;
			absence.setDateDernMaj(LocalDateTime.now()) ;
			absence.setModificateur(utilisateurCourant.getLogin()) ;
			absence.setEmploye(absenceExistante.getEmploye());
			absence.setDateAbsence(absenceExistante.getDateAbsence()) ;
			absence.setLibelleJustifiee(Booleen.getLibelle(absenceExistante.getJustifiee()));
			absence.setJustification(absenceExistante.getJustification());
			rest.put("http://pointage-zuul/pointage-server/ws/absence/maj", absence, Absence.class);
			return "redirect:/pointage/absences-aujourdhui" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absenceExistante", absenceExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/maj-absence" ;
		}
	}
	
	@GetMapping ("/pointage/details-absence/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Absence absence = rest.getForObject("http://pointage-zuul/pointage-server/ws/absence/id/{id}", 
				Absence.class, id) ;
		model.addAttribute("absenceExistante", absence) ;
		return "pointage/details-absence" ;
	}
	
	
	@PostMapping ("/pointage/absence/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Absence absenceExistant) throws Exception {
		try {
			Absence absence = rest.getForObject("http://pointage-zuul/pointage-server/ws/absence/id/{id}", 
					Absence.class, absenceExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", absence.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/absence/suppr/{id}", uriVariables);
			return "redirect:/pointage/absences" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absenceExistant", absenceExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "pointage/details-absence" ;
		}
	}
	
	private void initDependencies (Model model) {
		ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		List<Employe> listeEmployes = Arrays.asList(response.getBody());
		model.addAttribute("listeEmployes", listeEmployes) ; 
	}
	
}
