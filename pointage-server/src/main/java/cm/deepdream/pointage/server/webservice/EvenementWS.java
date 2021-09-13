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
import cm.deepdream.pointage.model.Evenement;
import cm.deepdream.pointage.server.service.EvenementService;

@RestController
@RequestMapping (path = "/ws/evenement")
public class EvenementWS {
	private Logger logger = Logger.getLogger(EvenementWS.class.getName()) ;
	@Autowired
	private EvenementService evenementService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Evenement ajout (@RequestBody Evenement evenement) {
		try {
			Evenement evenementCree = evenementService.creer(evenement) ;
			return evenementCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Evenement maj (@RequestBody Evenement evenement) {
		try {
			Evenement evenementMaj = evenementService.modifier(evenement) ;
			return evenementMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Evenement evenement = evenementService.rechercher(id) ;
			evenementService.supprimer(evenement) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Evenement getById (@PathVariable("id") long id) {
		try {
			Evenement evenement = evenementService.rechercher(id) ;
			return evenement ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/date/{date}")
	public List<Evenement> getMany(@PathVariable ("date") String dateStr)  {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Evenement> listeEvenements = evenementService.rechercher(date) ;
			return listeEvenements ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Evenement>() ;
		}
	}
	
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Evenement> getMany(@PathVariable ("from") String fromStr, @PathVariable ("to") String toStr)  {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Evenement> listeEvenements = evenementService.rechercher(from, to) ;
			return listeEvenements ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Evenement>() ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Evenement> getAll () {
		try {
			Evenement evenement = new Evenement() ;
			List<Evenement> listeEvenements = evenementService.rechercher(evenement) ;
			return listeEvenements ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Evenement>() ;
		}
	}
}
