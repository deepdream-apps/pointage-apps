package cm.deepdream.pointage.server.webservice;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.pointage.model.Absence;
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.server.service.AbsenceService;
import cm.deepdream.pointage.server.service.JourNonOuvreService;

@RestController
@RequestMapping (path = "/ws/absence")
public class AbsenceWS {
	private Logger logger = Logger.getLogger(AbsenceWS.class.getName()) ;
	@Autowired
	private AbsenceService absenceService ;
	@Autowired
	private JourNonOuvreService jourNonOuvreService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Absence ajout (@RequestBody Absence absence) {
		try {
			Absence absenceCree = absenceService.creer(absence) ;
			return absenceCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Absence maj (@RequestBody Absence absence) {
		try {
			Absence absenceMaj = absenceService.modifier(absence) ;
			return absenceMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Absence absence = absenceService.rechercher(id) ;
			absenceService.supprimer(absence) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Absence getById (@PathVariable("id") long id) {
		try {
			Absence absence = absenceService.rechercher(id) ;
			return absence ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Absence> getAll () {
		try {
			Absence absence = new Absence() ;
			List<Absence> listeAbsences = absenceService.rechercher(absence) ;
			return listeAbsences ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Absence>() ;
		}
	}
	
	@PostMapping("/enregistrement-absences")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public void enregistrerAbsences(@RequestBody LocalDate dateJour) throws Exception {
		try{
			logger.info("Lancement de l'enregistrement des absences du jour") ;
			if (! jourNonOuvreService.jourNonOuvre(dateJour)) {
				 absenceService.enregistrerAbsences(dateJour) ;
			}
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	@GetMapping("/date/{date}")
	public List<Absence> getByDate (@PathVariable("date") String dateStr) {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Absence> listeAbsences = absenceService.rechercher(date) ;
			return listeAbsences ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Absence>() ;
		}
	}
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Absence> getByDateBetween (@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Absence> listeAbsences = absenceService.rechercher(from, to) ;
			return listeAbsences ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Absence>() ;
		}
	}
}
