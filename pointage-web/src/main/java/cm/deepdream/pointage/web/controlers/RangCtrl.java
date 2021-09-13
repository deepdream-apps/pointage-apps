package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class RangCtrl implements Serializable{
	private Logger logger = Logger.getLogger(RangCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/rangs")
	public String index (Model model) throws Exception {
		 ResponseEntity<Rang[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/rang/all", Rang[].class) ;
		 List<Rang> listeRangs = Arrays.asList(response.getBody());
		 model.addAttribute("listeRangs", listeRangs) ;
		return "parametrage/rangs" ;
	}
	

	@GetMapping ("/parametrage/ajout-rang")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Rang rang = new Rang() ;
		model.addAttribute("rang", rang) ;
		return "parametrage/ajout-rang" ;
	}
	
	@PostMapping ("/parametrage/rang/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Rang rang) throws Exception {
		try {
			rang.setCreateur(utilisateurCourant.getLogin());
			rang.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/rang/ajout", rang, Rang.class);			
			return "redirect:/parametrage/rangs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("rang", rang) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-rang" ;
		}
	}
	
	@GetMapping ("/parametrage/maj-rang/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Rang rang = rest.getForObject("http://pointage-zuul/pointage-server/ws/rang/id/{id}", 
				Rang.class, id) ;
		model.addAttribute("rangExistant", rang) ;
		return "parametrage/maj-rang" ;
	}
	
	@PostMapping ("/parametrage/rang/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Rang rangExistant) throws Exception {
		try {
			Rang rang = rest.getForObject("http://pointage-zuul/pointage-server/ws/rang/id/{id}", 
					Rang.class, rangExistant.getId()) ;
			rang.setDateDernMaj(LocalDateTime.now()) ;
			rang.setModificateur(utilisateurCourant.getLogin()) ;
			rang.setLibelle(rangExistant.getLibelle());
			rest.put("http://pointage-zuul/pointage-server/ws/rang/maj", rang, Rang.class);
			return "redirect:/parametrage/rangs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("rangExistant", rangExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-rang" ;
		}
	}
	
	@GetMapping ("/parametrage/details-rang/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Rang rang = rest.getForObject("http://pointage-zuul/pointage-server/ws/rang/id/{id}", Rang.class, id) ;
		model.addAttribute("rangExistant", rang) ;
		return "parametrage/details-rang" ;
	}
	
	@PostMapping ("/parametrage/rang/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Rang rangExistant) throws Exception {
		try {
			Rang rang = rest.getForObject("http://pointage-zuul/pointage-server/ws/rang/id/{id}", 
					Rang.class, rangExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", rang.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/rang/suppr/{id}", uriVariables);
			return "redirect:/parametrage/rangs" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("rangExistant", rangExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-rang" ;
		}
	}
	
}
