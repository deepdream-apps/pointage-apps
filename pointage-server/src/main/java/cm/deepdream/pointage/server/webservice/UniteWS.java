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
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.server.service.UniteService;
@RestController
@RequestMapping (path = "/ws/unite")
public class UniteWS {
	private Logger logger = Logger.getLogger(UniteWS.class.getName()) ;
	@Autowired
	private UniteService uniteService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public UniteAdministrative ajout (@RequestBody UniteAdministrative unite) {
		try {
			UniteAdministrative uniteCree = uniteService.creer(unite) ;
			return uniteCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public UniteAdministrative maj (@RequestBody UniteAdministrative unite) {
		try {
			UniteAdministrative uniteMaj = uniteService.modifier(unite) ;
			return uniteMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			UniteAdministrative unite = uniteService.rechercher(id) ;
			uniteService.supprimer(unite) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public UniteAdministrative getById (@PathVariable("id") long id) {
		try {
			UniteAdministrative unite = uniteService.rechercher(id) ;
			return unite ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<UniteAdministrative> getAll () {
		try {
			UniteAdministrative unite = new UniteAdministrative() ;
			List<UniteAdministrative> listeStructures = uniteService.rechercher(unite) ;
			return listeStructures ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<UniteAdministrative>() ;
		}
	}
}
