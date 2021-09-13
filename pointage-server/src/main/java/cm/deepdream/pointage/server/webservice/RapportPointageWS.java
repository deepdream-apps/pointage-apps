package cm.deepdream.pointage.server.webservice;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.server.service.EmployeService;
import cm.deepdream.pointage.server.service.RapportPointageService;

@RestController
@RequestMapping (path = "/ws/rapport/pointage")
public class RapportPointageWS {
	private Logger logger = Logger.getLogger(RapportPointageWS.class.getName()) ;
	@Autowired
	private RapportPointageService rapportService ;
	@Autowired
	private EmployeService employeService ;
	
	@RequestMapping(value = "/from/{from}/to/{to}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> getByDateBetween (@PathVariable("from") String fromStr, 
			@PathVariable("to") String toStr) {
		try {
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			File file =  rapportService.genererRapportDetaille(from, to) ;
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");

		    return ResponseEntity
		            .ok()
		            .headers(headers)
		            .contentLength(file.length())
		            .contentType(MediaType.parseMediaType("application/pdf"))
		            .body(new InputStreamResource(new FileInputStream(file)));
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@RequestMapping(value = "/employe/{idEmploye}/from/{from}/to/{to}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> getByDateBetween (@PathVariable("idEmploye") Long idEmploye,
			@PathVariable("from") String fromStr, @PathVariable("to") String toStr) {
		try {
			Employe employe = employeService.rechercher(idEmploye) ;
			LocalDate from = LocalDate.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			LocalDate to = LocalDate.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
			File file = rapportService.genererRapport(employe, from, to) ;
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");

		    return ResponseEntity
		            .ok()
		            .headers(headers)
		            .contentLength(file.length())
		            .contentType(MediaType.parseMediaType("application/pdf"))
		            .body(new InputStreamResource(new FileInputStream(file)));
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	
	
}
