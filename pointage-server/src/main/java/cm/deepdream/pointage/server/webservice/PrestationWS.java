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

import cm.deepdream.pointage.enums.TypePrest;
import cm.deepdream.pointage.model.Prestation;
import cm.deepdream.pointage.server.service.PrestationService;

@RestController
@RequestMapping (path = "/ws/prestation")
public class PrestationWS {
	private Logger logger = Logger.getLogger(PrestationWS.class.getName()) ;
	@Autowired
	private PrestationService prestationService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Prestation ajout (@RequestBody Prestation prestation) {
		try {
			Prestation prestationCree = prestationService.creer(prestation) ;
			return prestationCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Prestation maj (@RequestBody Prestation prestation) {
		try {
			Prestation prestationMaj = prestationService.modifier(prestation) ;
			return prestationMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Prestation prestation = prestationService.rechercher(id) ;
			prestationService.supprimer(prestation) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Prestation getById (@PathVariable("id") long id) {
		try {
			Prestation prestation = prestationService.rechercher(id) ;
			return prestation ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Prestation> getAll () {
		try {
			Prestation prestation = new Prestation() ;
			List<Prestation> listePrestations = prestationService.rechercher(prestation) ;
			return listePrestations ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Prestation>() ;
		}
	}
	
	@GetMapping("/souscrit")
	public List<Prestation> getPrestationsSouscrites () {
		try {
			List<Prestation> listePrestations = prestationService.rechercher(TypePrest.Souscrit.getId()) ;
			return listePrestations ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Prestation>() ;
		}
	}
	
	@GetMapping("/offert")
	public List<Prestation> getPrestationsOffertes () {
		try {
			List<Prestation> listePrestations = prestationService.rechercher(TypePrest.Offert.getId()) ;
			return listePrestations ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Prestation>() ;
		}
	}
}
