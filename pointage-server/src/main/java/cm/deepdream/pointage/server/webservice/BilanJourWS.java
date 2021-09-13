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
import cm.deepdream.pointage.model.BilanJour;
import cm.deepdream.pointage.server.service.BilanJourService;

@RestController
@RequestMapping (path = "/ws/bilanjour")
public class BilanJourWS {
	private Logger logger = Logger.getLogger(BilanJourWS.class.getName()) ;
	@Autowired
	private BilanJourService bilanJourService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public BilanJour ajout (@RequestBody BilanJour bilanJour) {
		try {
			BilanJour bilanJourCree = bilanJourService.creer(bilanJour) ;
			return bilanJourCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public BilanJour maj (@RequestBody BilanJour bilanJour) {
		try {
			BilanJour bilanJourMaj = bilanJourService.modifier(bilanJour) ;
			return bilanJourMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			BilanJour bilanJour = bilanJourService.rechercher(id) ;
			bilanJourService.supprimer(bilanJour) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public BilanJour getById (@PathVariable("id") long id) {
		try {
			BilanJour bilanJour = bilanJourService.rechercher(id) ;
			return bilanJour ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<BilanJour> getAll () {
		try {
			BilanJour bilandHebdo = new BilanJour() ;
			List<BilanJour> listeBilandHebdos = bilanJourService.rechercher(bilandHebdo) ;
			return listeBilandHebdos ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<BilanJour>() ;
		}
	}
}
