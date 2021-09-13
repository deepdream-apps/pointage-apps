package cm.deepdream.pointage.web.webservice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.web.controlers.EmployeCtrl;

@RestController
@RequestMapping (path = "/webservice/employe")
public class EmployeWebService {
	private Logger logger = Logger.getLogger(EmployeCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@GetMapping("/all")
	public List<Employe> getAll () {
		try {
			ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
			List<Employe> listeEmployes = Arrays.asList(response.getBody());
			return listeEmployes ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Employe>() ;
		}
	}
	
	@PostMapping ("/parametrage/maj-photo/employe/{idEmploye}/sentiment/{idSentiment}/photo/{photo}")
	public void modifierPhoto (@PathVariable ("idEmploye") long idEmploye, @RequestBody Employe employeExistant) throws Exception {
		
		Employe employe = rest.getForObject("http://pointage-zuul/pointage-server/ws/employe/id/{id}", Employe.class, employeExistant.getId()) ;
		
		
		rest.postForEntity("http://pointage-zuul/pointage-server/ws/employe/maj", employe, Employe.class) ;
	}
	
}
