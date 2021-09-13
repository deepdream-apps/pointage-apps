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
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.server.service.StatutService;

@RestController
@RequestMapping (path = "/ws/statut")
public class StatutWS {
	private Logger logger = Logger.getLogger(StatutWS.class.getName()) ;
	@Autowired
	private StatutService statutService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Statut ajout (@RequestBody Statut statut) {
		try {
			Statut statutCree = statutService.creer(statut) ;
			return statutCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Statut maj (@RequestBody Statut statut) {
		try {
			Statut statutMaj = statutService.modifier(statut) ;
			return statutMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Statut statut = statutService.rechercher(id) ;
			statutService.supprimer(statut) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Statut getById (@PathVariable("id") long id) {
		try {
			Statut statut = statutService.rechercher(id) ;
			return statut ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Statut> getAll () {
		try {
			Statut statut = new Statut() ;
			List<Statut> listeStatuts = statutService.rechercher(statut) ;
			return listeStatuts ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Statut>() ;
		}
	}
}
