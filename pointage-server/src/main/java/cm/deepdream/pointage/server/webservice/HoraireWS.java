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
import cm.deepdream.pointage.model.Horaire;
import cm.deepdream.pointage.server.service.HoraireService;

@RestController
@RequestMapping (path = "/ws/horaire")
public class HoraireWS {
	private Logger logger = Logger.getLogger(HoraireWS.class.getName()) ;
	@Autowired
	private HoraireService horaireService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Horaire ajout (@RequestBody Horaire horaire) {
		try {
			Horaire horaireCree = horaireService.creer(horaire) ;
			return horaireCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Horaire maj (@RequestBody Horaire horaire) {
		try {
			Horaire horaireMaj = horaireService.modifier(horaire) ;
			return horaireMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Horaire horaire = horaireService.rechercher(id) ;
			horaireService.supprimer(horaire) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Horaire getById (@PathVariable("id") long id) {
		try {
			Horaire horaire = horaireService.rechercher(id) ;
			return horaire ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/statut/{idStatut}")
	public Horaire getByStatutId (@PathVariable("idStatut") long idStatut) {
		try {
			Horaire horaire = horaireService.rechercherStatut(idStatut) ;
			return horaire ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Horaire> getAll () {
		try {
			Horaire horaire = new Horaire() ;
			List<Horaire> listeHoraires = horaireService.rechercher(horaire) ;
			return listeHoraires ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Horaire>() ;
		}
	}
}
