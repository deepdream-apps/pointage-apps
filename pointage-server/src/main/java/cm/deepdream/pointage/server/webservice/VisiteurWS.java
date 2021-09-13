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
import cm.deepdream.pointage.model.Visiteur;
import cm.deepdream.pointage.server.service.VisiteurService;
@RestController
@RequestMapping (path = "/ws/visiteur")
public class VisiteurWS {
	private Logger logger = Logger.getLogger(VisiteurWS.class.getName()) ;
	@Autowired
	private VisiteurService visiteurService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Visiteur ajout (@RequestBody Visiteur visiteur) {
		try {
			Visiteur visiteurCree = visiteurService.creer(visiteur) ;
			return visiteurCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Visiteur maj (@RequestBody Visiteur visiteur) {
		try {
			Visiteur visiteurMaj = visiteurService.modifier(visiteur) ;
			return visiteurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Visiteur visiteur = visiteurService.rechercher(id) ;
			visiteurService.supprimer(visiteur) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Visiteur getById (@PathVariable("id") long id) {
		try {
			Visiteur visiteur = visiteurService.rechercher(id) ;
			return visiteur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Visiteur> getAll () {
		try {
			Visiteur visiteur = new Visiteur() ;
			List<Visiteur> listeVisiteurs = visiteurService.rechercher(visiteur) ;
			return listeVisiteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visiteur>() ;
		}
	}
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Visiteur> getByDateBetween (@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Visiteur> listeVisiteurs = visiteurService.rechercher(from, to) ;
			return listeVisiteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visiteur>() ;
		}
	}
	
	
	@GetMapping("/date/{date}")
	public List<Visiteur> getByDate (@PathVariable("date") String dateStr) {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Visiteur> listeVisiteurs = visiteurService.rechercher(date) ;
			return listeVisiteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visiteur>() ;
		}
	}
	
	@GetMapping("/frequents/{nombre}")
	public List<Visiteur> visiteursFrequents (@PathVariable("nombre") int nombre) {
		try {
			List<Visiteur> listeVisiteurs = visiteurService.rechercherVisiteursFrequents(nombre) ;
			return listeVisiteurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visiteur>() ;
		}
	}
	
}
