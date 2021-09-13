package cm.deepdream.pointage.server.webservice;
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
import cm.deepdream.pointage.model.JourNonOuvre;
import cm.deepdream.pointage.server.service.JourNonOuvreService;

@RestController
@RequestMapping (path = "/ws/journonouvre")
public class JourNonOuvreWS {
	private Logger logger = Logger.getLogger(JourNonOuvreWS.class.getName()) ;
	@Autowired
	private JourNonOuvreService jourNonOuvreService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public JourNonOuvre ajout (@RequestBody JourNonOuvre jourNonOuvre) {
		try {
			JourNonOuvre jourNonOuvreCree = jourNonOuvreService.creer(jourNonOuvre) ;
			return jourNonOuvreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public JourNonOuvre maj (@RequestBody JourNonOuvre jourNonOuvre) {
		try {
			JourNonOuvre jourNonOuvreMaj = jourNonOuvreService.modifier(jourNonOuvre) ;
			return jourNonOuvreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			JourNonOuvre jourNonOuvre = jourNonOuvreService.rechercher(id) ;
			jourNonOuvreService.supprimer(jourNonOuvre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public JourNonOuvre getById (@PathVariable("id") long id) {
		try {
			JourNonOuvre jourNonOuvre = jourNonOuvreService.rechercher(id) ;
			return jourNonOuvre ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<JourNonOuvre> getAll () {
		try {
			JourNonOuvre jourNonOuvre = new JourNonOuvre() ;
			List<JourNonOuvre> listeJoursNonOuvres = jourNonOuvreService.rechercher(jourNonOuvre) ;
			return listeJoursNonOuvres ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<JourNonOuvre>() ;
		}
	}
}
