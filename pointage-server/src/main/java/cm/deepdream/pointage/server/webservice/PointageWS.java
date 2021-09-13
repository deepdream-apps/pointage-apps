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
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.server.service.PointageService;

@RestController
@RequestMapping (path = "/ws/pointage")
public class PointageWS {
	private Logger logger = Logger.getLogger(PointageWS.class.getName()) ;
	@Autowired
	private PointageService pointageService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Pointage ajout (@RequestBody Pointage pointage) {
		try {
			Pointage pointageCree = pointageService.creer(pointage) ;
			return pointageCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Pointage maj (@RequestBody Pointage pointage) {
		try {
			Pointage pointageMaj = pointageService.modifier(pointage) ;
			return pointageMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Pointage pointage = pointageService.rechercher(id) ;
			pointageService.supprimer(pointage) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Pointage getById (@PathVariable("id") long id) {
		try {
			Pointage pointage = pointageService.rechercher(id) ;
			return pointage ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Pointage> getAll () {
		try {
			Pointage pointage = new Pointage() ;
			List<Pointage> listePointages = pointageService.rechercher(pointage) ;
			return listePointages ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pointage>() ;
		}
	}
	
	@GetMapping("/date/{date}")
	public List<Pointage> getByDate (@PathVariable("date") String dateStr) {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Pointage> listePointages = pointageService.rechercher(date) ;
			return listePointages ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pointage>() ;
		}
	}
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Pointage> getByDateBetween (@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Pointage> listePointages = pointageService.rechercher(from, to) ;
			return listePointages ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Pointage>() ;
		}
	}
}
