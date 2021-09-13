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
import cm.deepdream.pointage.model.BilanHebdo;
import cm.deepdream.pointage.server.service.BilanHebdoService;

@RestController
@RequestMapping (path = "/ws/bilandHebdo")
public class BilanHebdoWS {
	private Logger logger = Logger.getLogger(BilanHebdoWS.class.getName()) ;
	@Autowired
	private BilanHebdoService bilanHebdoService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public BilanHebdo ajout (@RequestBody BilanHebdo bilanHebdo) {
		try {
			BilanHebdo bilanHebdoCree = bilanHebdoService.creer(bilanHebdo) ;
			return bilanHebdoCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public BilanHebdo maj (@RequestBody BilanHebdo bilanHebdo) {
		try {
			BilanHebdo bilanHebdoMaj = bilanHebdoService.modifier(bilanHebdo) ;
			return bilanHebdoMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			BilanHebdo bilanHebdo = bilanHebdoService.rechercher(id) ;
			bilanHebdoService.supprimer(bilanHebdo) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public BilanHebdo getById (@PathVariable("id") long id) {
		try {
			BilanHebdo bilanHebdo = bilanHebdoService.rechercher(id) ;
			return bilanHebdo ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<BilanHebdo> getAll () {
		try {
			BilanHebdo bilandHebdo = new BilanHebdo() ;
			List<BilanHebdo> listeBilandHebdos = bilanHebdoService.rechercher(bilandHebdo) ;
			return listeBilandHebdos ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<BilanHebdo>() ;
		}
	}
}
