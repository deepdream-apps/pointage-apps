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
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.server.service.UtilisateurService;
@RestController
@RequestMapping (path = "/ws/utilisateur")
public class UtilisateurWS {
	private Logger logger = Logger.getLogger(UtilisateurWS.class.getName()) ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Utilisateur ajout (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurCree = utilisateurService.creer(utilisateur) ;
			return utilisateurCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Utilisateur maj (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurMaj = utilisateurService.modifier(utilisateur) ;
			return utilisateurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj-mot-de-passe")
	@ResponseStatus(code =  HttpStatus.OK)
	public Utilisateur majMdp (@RequestBody Utilisateur utilisateur) {
		try {
			Utilisateur utilisateurMaj = utilisateurService.modifier(utilisateur) ;
			return utilisateurMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			utilisateurService.supprimer(utilisateur) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Utilisateur getById (@PathVariable("id") long id) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/login/{login}")
	public Utilisateur getByLogin (@PathVariable("login") String login) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(login) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Utilisateur> getAll () {
		try {
			Utilisateur utilisateur = new Utilisateur() ;
			List<Utilisateur> listeUtilisateurs = utilisateurService.rechercher(utilisateur) ;
			return listeUtilisateurs ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Utilisateur>() ;
		}
	}
	
	@GetMapping("/activate/{id}")
	public Utilisateur activate (@PathVariable("id") long id) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			Utilisateur utilisateurActive = utilisateurService.activer(utilisateur) ;
			return utilisateurActive ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/desactivate/{id}")
	public Utilisateur desactivate (@PathVariable("id") long id) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			Utilisateur utilisateurDesactive = utilisateurService.desactiver(utilisateur) ;
			return utilisateurDesactive ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
}
