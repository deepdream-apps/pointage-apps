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
import cm.deepdream.pointage.model.Session;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.server.service.SessionService;
import cm.deepdream.pointage.server.service.UtilisateurService;
@RestController
@RequestMapping (path = "/ws/session")
public class SessionWS {
	private Logger logger = Logger.getLogger(SessionWS.class.getName()) ;
	@Autowired
	private SessionService sessionService ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Session ajout (@RequestBody Session session) {
		try {
			Session sessionCree = sessionService.creer(session) ;
			return sessionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Session maj (@RequestBody Session session) {
		try {
			Session sessionMaj = sessionService.modifier(session) ;
			return sessionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/terminer-session/{login}")
	@ResponseStatus(code =  HttpStatus.OK)
	public Session terminer (@PathVariable("login") String login) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercher(login) ;
			Session sessionMaj = sessionService.terminer(utilisateur) ;
			return sessionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Session session = sessionService.rechercher(id) ;
			sessionService.supprimer(session) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Session getById (@PathVariable("id") long id) {
		try {
			Session session = sessionService.rechercher(id) ;
			return session ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Session> getAll () {
		try {
			Session session = new Session() ;
			List<Session> listeSessions = sessionService.rechercher(session) ;
			return listeSessions ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Session>() ;
		}
	}
}
