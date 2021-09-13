package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

@Controller
@SessionAttributes({"utilisateurCourant"})
public class RapportPointageCtrl implements Serializable{
	private Logger logger = Logger.getLogger(RapportPointageCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	
	@GetMapping ("/pointage/pointages-aujourdhui2")
	public String rapport1 (Model model) throws Exception {
		try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<byte[]> response = rest.exchange("http://pointage-zuul/pointage-server/ws/rapport/pointage/employe/{idEmploye}/from/{from}/to/{to}", 
	        		HttpMethod.GET, entity, byte[].class);
	        Files.write(Paths.get("e:\\download-files\\demo1.pdf"), response.getBody());
	     }catch (Exception e){
	           e.printStackTrace();
	     }
		return null ;
	}
	
	
}
