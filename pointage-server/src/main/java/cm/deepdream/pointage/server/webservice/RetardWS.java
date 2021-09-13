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
import cm.deepdream.pointage.model.Retard;
import cm.deepdream.pointage.server.service.RetardService;

@RestController
@RequestMapping (path = "/ws/retard")
public class RetardWS {
	private Logger logger = Logger.getLogger(RetardWS.class.getName()) ;
	@Autowired
	private RetardService retardService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Retard ajout (@RequestBody Retard retard) {
		try {
			Retard retardCree = retardService.creer(retard) ;
			return retardCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Retard maj (@RequestBody Retard retard) {
		try {
			Retard retardMaj = retardService.modifier(retard) ;
			return retardMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Retard retard = retardService.rechercher(id) ;
			retardService.supprimer(retard) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Retard getById (@PathVariable("id") long id) {
		try {
			Retard retard = retardService.rechercher(id) ;
			return retard ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/date/{date}")
	public List<Retard> getByDate (@PathVariable("date") String dateStr) {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Retard> listeRetards = retardService.rechercher(date) ;
			return listeRetards ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Retard>() ;
		}
	}
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Retard> getByDateBetween (@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Retard> listeRetards = retardService.rechercher(from, to) ;
			return listeRetards ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Retard>() ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Retard> getAll () {
		try {
			Retard retard = new Retard() ;
			List<Retard> listeRetards = retardService.rechercher(retard) ;
			return listeRetards ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Retard>() ;
		}
	}
}
