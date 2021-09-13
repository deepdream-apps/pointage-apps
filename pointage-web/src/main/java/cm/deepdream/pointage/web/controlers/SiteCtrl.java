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
import cm.deepdream.pointage.model.Site;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class SiteCtrl implements Serializable{
	private Logger logger = Logger.getLogger(SiteCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/sites")
	public String index (Model model) throws Exception {
		 ResponseEntity<Site[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/site/all", Site[].class) ;
		 List<Site> listeSites = Arrays.asList(response.getBody());
		 model.addAttribute("listeSites", listeSites) ;
		 return "parametrage/sites" ;
	}
	

	@GetMapping ("/parametrage/ajout-site")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception { 
		Site site = new Site() ;
		model.addAttribute("site", site) ;
		return "parametrage/ajout-site" ;
	}
	
	@PostMapping ("/parametrage/site/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Site site) throws Exception {
		try {
			site.setCreateur(utilisateurCourant.getLogin());
			site.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/site/ajout", site, Site.class);			
			return "redirect:/parametrage/sites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("site", site) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-site" ;
		}
	}
	
	@GetMapping ("/parametrage/maj-site/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Site site = rest.getForObject("http://pointage-zuul/pointage-server/ws/site/id/{id}", Site.class, id) ;

		model.addAttribute("siteExistant", site) ;
		return "parametrage/maj-site" ;
	}
	
	@PostMapping ("/parametrage/site/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Site siteExistant) throws Exception {
		try {
			Site site = rest.getForObject("http://pointage-zuul/pointage-server/ws/site/id/{id}", 
					Site.class, siteExistant.getId()) ;
			site.setDateDernMaj(LocalDateTime.now()) ;
			site.setModificateur(utilisateurCourant.getLogin()) ;
			site.setLocalisation(siteExistant.getLocalisation()) ;
			site.setLibelle(siteExistant.getLibelle()) ;
			site.setCode(siteExistant.getCode());
			rest.put("http://pointage-zuul/pointage-server/ws/site/maj", site, Site.class);
			return "redirect:/parametrage/sites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("siteExistant", siteExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-site" ;
		}
	}
	
	@GetMapping ("/parametrage/details-site/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Site site = rest.getForObject("http://pointage-zuul/pointage-server/ws/site/id/{id}", Site.class, id) ;
		model.addAttribute("siteExistant", site) ;
		return "parametrage/details-site" ;
	}
	
	
	@PostMapping ("/parametrage/site/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Site siteExistant) throws Exception {
		try {
			Site site = rest.getForObject("http://pointage-zuul/pointage-server/ws/site/id/{id}", 
					Site.class, siteExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", site.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/site/suppr/{id}", uriVariables);
			return "redirect:/parametrage/sites" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("siteExistant", siteExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-site" ;
		}
	}
	
}
