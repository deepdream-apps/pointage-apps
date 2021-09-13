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
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.server.service.ProfilService;

@RestController
@RequestMapping (path = "/ws/profil")
public class ProfilWS {
	private Logger logger = Logger.getLogger(ProfilWS.class.getName()) ;
	@Autowired
	private ProfilService profilService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Profil ajout (@RequestBody Profil profil) {
		try {
			Profil profilCree = profilService.creer(profil) ;
			return profilCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Profil maj (@RequestBody Profil profil) {
		try {
			Profil profilMaj = profilService.modifier(profil) ;
			return profilMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Profil profil = profilService.rechercher(id) ;
			profilService.supprimer(profil) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Profil getById (@PathVariable("id") long id) {
		try {
			Profil profil = profilService.rechercher(id) ;
			return profil ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Profil> getAll () {
		try {
			Profil profil = new Profil() ;
			List<Profil> listeProfils = profilService.rechercher(profil) ;
			return listeProfils ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Profil>() ;
		}
	}
}
