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
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.server.service.EmployeService;
import cm.deepdream.pointage.server.service.PosteService;
import cm.deepdream.pointage.server.service.StatutService;
import cm.deepdream.pointage.server.service.UniteService;

@RestController
@RequestMapping (path = "/ws/employe")
public class EmployeWS {
	private Logger logger = Logger.getLogger(EmployeWS.class.getName()) ;
	@Autowired
	private EmployeService employeService ;
	@Autowired
	private StatutService statutService ;
	@Autowired 
	private UniteService uniteService ;
	@Autowired
	private PosteService posteService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Employe ajout (@RequestBody Employe employe) {
		try {
			employe.setStatut(statutService.rechercher(employe.getStatut().getId()));
			employe.setUnite(uniteService.rechercher(employe.getUnite().getId()));
			employe.setPoste(posteService.rechercher(employe.getPoste().getId()));
			Employe employeCree = employeService.creer(employe) ;
			return employeCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Employe maj (@RequestBody Employe employe) {
		try {
			employe.setStatut(statutService.rechercher(employe.getStatut().getId()));
			employe.setUnite(uniteService.rechercher(employe.getUnite().getId()));
			employe.setPoste(posteService.rechercher(employe.getPoste().getId()));
			Employe employeMaj = employeService.modifier(employe) ;
			return employeMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Employe employe = employeService.rechercher(id) ;
			employeService.supprimer(employe) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Employe getById (@PathVariable("id") long id) {
		try {
			Employe employe = employeService.rechercher(id) ;
			return employe ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Employe> getAll () {
		try {
			Employe employe = new Employe() ;
			List<Employe> listeEmployes = employeService.rechercher(employe) ;
			return listeEmployes ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Employe>() ;
		}
	}
}
