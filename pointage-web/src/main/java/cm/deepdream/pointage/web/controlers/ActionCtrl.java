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
import cm.deepdream.pointage.model.Action;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class ActionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ActionCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/admin/actions")
	public String index (Model model) throws Exception {
		 ResponseEntity<Action[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/action/all", Action[].class) ;
		 List<Action> listeActions = Arrays.asList(response.getBody());
		 model.addAttribute("listeActions", listeActions) ;
		return "admin/actions" ;
	}
	

	@GetMapping ("/admin/ajout-action")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Action action = new Action() ;
		model.addAttribute("action", action) ;
		return "admin/ajout-action" ;
	}
	
	@PostMapping ("/admin/action/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Action action) throws Exception {
		try {
			action.setCreateur(utilisateurCourant.getLogin());
			action.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/action/ajout", action, Action.class);			
			return "redirect:/admin/actions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("action", action) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/ajout-action" ;
		}
	}
	
	@GetMapping ("/admin/maj-action/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Action action = rest.getForObject("http://pointage-zuul/pointage-server/ws/action/id/{id}", Action.class, id) ;
		model.addAttribute("actionExistante", action) ;
		return "admin/maj-action" ;
	}
	
	@PostMapping ("/admin/action/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Action actionExistante) throws Exception {
		try {
			Action action = rest.getForObject("http://pointage-zuul/pointage-server/ws/action/id/{id}", 
					Action.class, actionExistante.getId()) ;
			action.setDateDernMaj(LocalDateTime.now()) ;
			action.setModificateur(utilisateurCourant.getLogin()) ;
			action.setCode(actionExistante.getCode());
			action.setLibelle(actionExistante.getLibelle()) ;
			action.setDescription(actionExistante.getDescription());
			rest.put("http://pointage-zuul/pointage-server/ws/action/maj", action, Action.class);
			return "redirect:/admin/actions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("actionExistant", actionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/maj-action" ;
		}
	}
	
	@GetMapping ("/admin/details-action/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Action action = rest.getForObject("http://pointage-zuul/pointage-server/ws/action/id/{id}", Action.class, id) ;
		model.addAttribute("actionExistante", action) ;
		return "admin/details-action" ;
	}
	
	
	@PostMapping ("/admin/action/suppr")
	public String suppr (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Action actionExistante) throws Exception {
		try {
			Action action = rest.getForObject("http://pointage-zuul/pointage-server/ws/action/id/{id}", Action.class, actionExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", action.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/action/suppr/{id}", uriVariables);
			return "redirect:/admin/actions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("actionExistante", actionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/details-action" ;
		}
	}
	
}
