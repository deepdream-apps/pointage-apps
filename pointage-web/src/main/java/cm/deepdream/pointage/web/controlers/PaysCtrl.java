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
import cm.deepdream.pointage.model.Pays;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class PaysCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PaysCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/pays")
	public String index (Model model) throws Exception {
		 ResponseEntity<Pays[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/pays/all", Pays[].class) ;
		 List<Pays> listePayss = Arrays.asList(response.getBody());
		 model.addAttribute("listePays", listePayss) ;
		 return "parametrage/pays" ;
	}
	

	@GetMapping ("/parametrage/ajout-pays")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception { 
		Pays pays = new Pays() ;
		model.addAttribute("pays", pays) ;
		return "parametrage/ajout-pays" ;
	}
	
	@PostMapping ("/parametrage/pays/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Pays pays) throws Exception {
		try {
			pays.setCreateur(utilisateurCourant.getLogin());
			pays.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/pays/ajout", pays, Pays.class);			
			return "redirect:/parametrage/pays" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("pays", pays) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-pays" ;
		}
	}
	
	@GetMapping ("/parametrage/maj-pays/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Pays pays = rest.getForObject("http://pointage-zuul/pointage-server/ws/pays/id/{id}", Pays.class, id) ;

		model.addAttribute("paysExistant", pays) ;
		return "parametrage/maj-pays" ;
	}
	
	@PostMapping ("/parametrage/pays/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Pays paysExistant) throws Exception {
		try {
			Pays pays = rest.getForObject("http://pointage-zuul/pointage-server/ws/pays/id/{id}", 
					Pays.class, paysExistant.getId()) ;
			pays.setDateDernMaj(LocalDateTime.now()) ;
			pays.setModificateur(utilisateurCourant.getLogin()) ;
			pays.setCode(paysExistant.getCode()) ;
			pays.setLibelle(paysExistant.getLibelle()) ;
			rest.put("http://pointage-zuul/pointage-server/ws/pays/maj", pays, Pays.class);
			return "redirect:/parametrage/pays" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("paysExistant", paysExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-pays" ;
		}
	}
	
	@GetMapping ("/parametrage/details-pays/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Pays pays = rest.getForObject("http://pointage-zuul/pointage-server/ws/pays/id/{id}", Pays.class, id) ;
		model.addAttribute("paysExistant", pays) ;
		return "parametrage/details-pays" ;
	}
	
	
	@PostMapping ("/parametrage/pays/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Pays paysExistant) throws Exception {
		try {
			Pays pays = rest.getForObject("http://pointage-zuul/pointage-server/ws/pays/id/{id}", 
					Pays.class, paysExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", pays.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/pays/suppr/{id}", uriVariables);
			return "redirect:/parametrage/pays" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("paysExistant", paysExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-pays" ;
		}
	}
	
}
