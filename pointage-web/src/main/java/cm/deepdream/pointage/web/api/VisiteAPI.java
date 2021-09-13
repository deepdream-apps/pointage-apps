package cm.deepdream.pointage.web.api;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.model.Visite;
@RestController
@RequestMapping(path = "/webapi/visite")
public class VisiteAPI {
	private Logger logger = Logger.getLogger(VisiteAPI.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	@GetMapping("/typeId/{typeId}/numeroId/{numeroId}")
	public List<Visite> getVisite (@PathVariable("typeId") String typeId, 
			@PathVariable("numeroId") String numeroId) {
		try {
			ResponseEntity<Visite[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/visite/typeId/{typeId}/numeroId/{numeroId}", 
					Visite[].class, typeId, numeroId) ;
			List<Visite> listeVisites = Arrays.asList(response.getBody());
			return listeVisites ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
}
