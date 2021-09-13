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
import cm.deepdream.pointage.model.BilanMensuel;
import cm.deepdream.pointage.server.service.BilanMensuelService;

@RestController
@RequestMapping (path = "/ws/bilanmensuel")
public class BilanMensuelWS {
	private Logger logger = Logger.getLogger(BilanMensuelWS.class.getName()) ;
	@Autowired
	private BilanMensuelService bilanMensuelService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public BilanMensuel ajout (@RequestBody BilanMensuel bilanMensuel) {
		try {
			BilanMensuel bilanMensuelCree = bilanMensuelService.creer(bilanMensuel) ;
			return bilanMensuelCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public BilanMensuel maj (@RequestBody BilanMensuel bilanMensuel) {
		try {
			BilanMensuel bilanMensuelMaj = bilanMensuelService.modifier(bilanMensuel) ;
			return bilanMensuelMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			BilanMensuel bilanMensuel = bilanMensuelService.rechercher(id) ;
			bilanMensuelService.supprimer(bilanMensuel) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public BilanMensuel getById (@PathVariable("id") long id) {
		try {
			BilanMensuel bilanMensuel = bilanMensuelService.rechercher(id) ;
			return bilanMensuel ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@GetMapping("/all")
	public List<BilanMensuel> getAll () {
		try {
			BilanMensuel bilandHebdo = new BilanMensuel() ;
			List<BilanMensuel> listeBilandHebdos = bilanMensuelService.rechercher(bilandHebdo) ;
			return listeBilandHebdos ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<BilanMensuel>() ;
		}
	}
}
