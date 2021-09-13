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
import cm.deepdream.pointage.model.Poste;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class PosteCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PosteCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/personnel/postes")
	public String index (Model model) throws Exception {
		 ResponseEntity<Poste[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/poste/all", Poste[].class) ;
		 List<Poste> listePostes = Arrays.asList(response.getBody());
		 model.addAttribute("listePostes", listePostes) ;
		 return "personnel/postes" ;
	}
	

	@GetMapping ("/personnel/ajout-poste")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		
		initDependencies(model) ;
		Poste poste = new Poste() ;
		model.addAttribute("poste", poste) ;
		return "personnel/ajout-poste" ;
	}
	
	@PostMapping ("/personnel/poste/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Poste poste) throws Exception {
		try {
			poste.setCreateur(utilisateurCourant.getLogin());
			poste.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/poste/ajout", poste, Poste.class);			
			return "redirect:/personnel/postes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("poste", poste) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/ajout-poste" ;
		}
	}
	
	@GetMapping ("/personnel/maj-poste/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Poste poste = rest.getForObject("http://pointage-zuul/pointage-server/ws/poste/id/{id}", Poste.class, id) ;
		
		initDependencies(model) ;
		model.addAttribute("posteExistant", poste) ;
		return "personnel/maj-poste" ;
	}
	
	@PostMapping ("/personnel/poste/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Poste posteExistant) throws Exception {
		try {
			Poste poste = rest.getForObject("http://pointage-zuul/pointage-server/ws/poste/id/{id}", 
					Poste.class, posteExistant.getId()) ;
			poste.setDateDernMaj(LocalDateTime.now()) ;
			poste.setModificateur(utilisateurCourant.getLogin()) ;
			poste.setAbreviation(posteExistant.getAbreviation()) ;
			poste.setLibelle(posteExistant.getLibelle()) ;
			poste.setRang(posteExistant.getRang()) ;
			poste.setUniteAdministrative(posteExistant.getUniteAdministrative());
			rest.put("http://pointage-zuul/pointage-server/ws/poste/maj", poste, Poste.class);
			return "redirect:/personnel/postes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("posteExistant", posteExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/maj-poste" ;
		}
	}
	
	@GetMapping ("/personnel/details-poste/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Poste poste = rest.getForObject("http://pointage-zuul/pointage-server/ws/poste/id/{id}", 
				Poste.class, id) ;
		model.addAttribute("posteExistant", poste) ;
		return "personnel/details-poste" ;
	}
	
	
	@PostMapping ("/personnel/poste/suppr")
	public String supprimer (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Poste posteExistant) throws Exception {
		try {
			Poste poste = rest.getForObject("http://pointage-zuul/pointage-server/ws/poste/id/{id}", 
					Poste.class, posteExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", poste.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/poste/suppr/{id}", uriVariables);
			return "redirect:/personnel/postes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("posteExistant", posteExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/details-poste" ;
		}
	}
	
	private void initDependencies(Model model) {
		ResponseEntity<Rang[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/rang/all", Rang[].class) ;
		List<Rang> listeRangs = Arrays.asList(response.getBody());
		model.addAttribute("listeRangs", listeRangs) ;
		
		ResponseEntity<UniteAdministrative[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/unite/all", UniteAdministrative[].class) ;
		List<UniteAdministrative> listeUnites = Arrays.asList(response2.getBody());
		model.addAttribute("listeUnites", listeUnites) ;
	}
	
}
