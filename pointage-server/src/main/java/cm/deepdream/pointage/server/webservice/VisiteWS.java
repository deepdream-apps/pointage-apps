package cm.deepdream.pointage.server.webservice;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.server.service.VisiteService;
@RestController
@RequestMapping (path = "/ws/visite")
public class VisiteWS {
	private Logger logger = Logger.getLogger(VisiteWS.class.getName()) ;
	@Autowired
	private VisiteService visiteService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Visite ajout (@RequestBody Visite visite) {
		try {
			Visite visiteCree = visiteService.creer(visite) ;
			return visiteCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Visite maj (@RequestBody Visite visite) {
		try {
			Visite visiteMaj = visiteService.modifier(visite) ;
			return visiteMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Visite visite = visiteService.rechercher(id) ;
			visiteService.supprimer(visite) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Visite getById (@PathVariable("id") long id) {
		try {
			Visite visite = visiteService.rechercher(id) ;
			return visite ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/all")
	public List<Visite> getAll () {
		try {
			Visite visite = new Visite() ;
			List<Visite> listeVisites = visiteService.rechercher(visite) ;
			return listeVisites ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visite>() ;
		}
	}
	
	@GetMapping("/from/{from}/to/{to}")
	public List<Visite> getByDateBetween (@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Visite> listeVisites = visiteService.rechercher(from, to) ;
			return listeVisites ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visite>() ;
		}
	}
	
	
	@GetMapping("/date/{date}")
	public List<Visite> getByDate (@PathVariable("date") String dateStr) {
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			List<Visite> listeVisites = visiteService.rechercher(date) ;
			return listeVisites ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Visite>() ;
		}
	}
	
	@GetMapping("/typeId/{typeId}/numeroId/{numeroId}")
	public List<Visite> getVisite (@PathVariable("typeId") String typeId, @PathVariable("numeroId") String numeroId) {
		try {
			List<Visite> visites = visiteService.rechercher(typeId, numeroId) ;
			return visites ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
}
