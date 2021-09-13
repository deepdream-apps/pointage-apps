package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import cm.deepdream.pointage.model.Permission;
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class ProfilCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ProfilCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/admin/profils")
	public String index (Model model) throws Exception {
		 ResponseEntity<Profil[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/profil/all", Profil[].class) ;
		 List<Profil> listeProfils = Arrays.asList(response.getBody());
		 model.addAttribute("listeProfils", listeProfils) ;
		return "admin/profils" ;
	}
	

	@GetMapping ("/admin/ajout-profil")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		initDependencies (model) ;
		Profil profil = new Profil() ;
		model.addAttribute("profil", profil) ;
		return "admin/ajout-profil" ;
	}
	
	@PostMapping ("/admin/profil/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Profil profil) throws Exception {
		try {
			profil.setCreateur(utilisateurCourant.getLogin());
			profil.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/profil/ajout", profil, Profil.class);			
			return "redirect:/admin/profils" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("profil", profil) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/ajout-profil" ;
		}
	}
	
	@GetMapping ("/admin/maj-profil/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		initDependencies (model) ;
		Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", 
				Profil.class, id) ;
		model.addAttribute("profilExistant", profil) ;
		return "admin/maj-profil" ;
	}
	
	@PostMapping ("/admin/profil/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Profil profilExistant) throws Exception {
		try {
			Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", Profil.class, profilExistant.getId()) ;
			profil.setDateDernMaj(LocalDateTime.now()) ;
			profil.setModificateur(utilisateurCourant.getLogin()) ;
			profil.setLibelle(profilExistant.getLibelle());
			profil.setDescription(profilExistant.getDescription());
			rest.put("http://pointage-zuul/pointage-server/ws/profil/maj", profil, Profil.class);
			return "redirect:/admin/profils" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("profilExistant", profilExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/maj-profil" ;
		}
	}
	
	@GetMapping ("/admin/edition-permissions/{id}")
	public String editionPermissions (Model model, @PathVariable("id") long id) throws Exception {
		Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", Profil.class, id) ;
		model.addAttribute("profilExistant", profil) ;
		
		Permission permission = new Permission() ;
		permission.setProfil(profil);
		model.addAttribute("permissionCourante", permission) ;
		ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, profil.getId()) ;
		List<Permission> permissions = Arrays.asList(response.getBody());
		model.addAttribute("listePermissions", permissions) ;
		initDependencies (model) ;
		return "admin/edition-permissions" ;
	}
	
	@PostMapping ("/admin/profil/maj-permission")
	public String majPermission (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Permission permissionCourante) throws Exception {
		try {
			logger.info("Edition d'une permission") ;
			permissionCourante.setDateDernMaj(LocalDateTime.now()) ;
			permissionCourante.setModificateur(utilisateurCourant.getLogin()) ;
			if(permissionCourante.getId() == null)
				rest.postForObject("http://pointage-zuul/pointage-server/ws/permission/ajout", permissionCourante, Permission.class) ;
			else 
				rest.put("http://pointage-zuul/pointage-server/ws/permission/maj", permissionCourante, Permission.class) ;
			
			Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", Profil.class, permissionCourante.getProfil().getId()) ;
			permissionCourante = new Permission() ;
			permissionCourante.setProfil(profil);
			model.addAttribute("permissionCourante", permissionCourante) ;
			model.addAttribute("profilExistant", profil) ;
			ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, profil.getId()) ;
			List<Permission> permissions = Arrays.asList(response.getBody());
			model.addAttribute("listePermissions", permissions) ;
			initDependencies (model) ;
			return "admin/edition-permissions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("permissionCourante", permissionCourante) ;
			model.addAttribute("profilExistant", permissionCourante.getProfil()) ;
			ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, permissionCourante.getProfil().getId()) ;
			List<Permission> listePermissions = Arrays.asList(response.getBody());
			model.addAttribute("listePermissions", listePermissions) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/edition-permissions" ;
		}
	}
	
	@GetMapping ("/admin/suppr-permission/{id}")
	public String supprPermission (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@PathVariable("id") long id) throws Exception {
		Permission permission = null ;
		try {
			permission = rest.getForObject("http://pointage-zuul/pointage-server/ws/permission/id/{id}", Permission.class, id) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", permission.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/permission/suppr/{id}", uriVariables) ;
			
			Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", Profil.class, permission.getProfil().getId()) ;
			permission = new Permission() ;
			permission.setProfil(profil) ;
			model.addAttribute("permissionCourante", permission) ;
			model.addAttribute("profilExistant", profil) ;
			ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, profil.getId()) ;
			List<Permission> listePermissions = Arrays.asList(response.getBody());
			model.addAttribute("listePermissions", listePermissions) ;
			initDependencies (model) ;
			return "admin/edition-permissions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model) ;
			model.addAttribute("permissionCourante", permission) ;
			model.addAttribute("profilExistant", permission.getProfil()) ;
			ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, 
					permission.getProfil().getId()) ;
			List<Permission> listePermissions = Arrays.asList(response.getBody());
			model.addAttribute("listePermissions", listePermissions) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/edition-permissions" ;
		}
	}
	
	@GetMapping ("/admin/details-profil/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", Profil.class, id) ;
		model.addAttribute("profilExistant", profil) ;
		
		ResponseEntity<Permission[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/permission/profil/{idProfil}", Permission[].class, profil.getId()) ;
		List<Permission> permissions = Arrays.asList(response.getBody());
		model.addAttribute("listePermissions", permissions) ;
		
		return "admin/details-profil" ;
	}
	
	@PostMapping ("/admin/profil/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Profil profilExistant) throws Exception {
		try {
			Profil profil = rest.getForObject("http://pointage-zuul/pointage-server/ws/profil/id/{id}", 
					Profil.class, profilExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", profil.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/profil/suppr/{id}", uriVariables);
			return "redirect:/admin/profils" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("profilExistant", profilExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "admin/details-profil" ;
		}
	}
	
	private void initDependencies (Model model) {
		 ResponseEntity<Statut[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/statut/all", Statut[].class) ;
		 List<Statut> listeStatuts = Arrays.asList(response.getBody());
		 model.addAttribute("listeStatuts", listeStatuts) ;
		 
		 ResponseEntity<Action[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/action/all", Action[].class) ;
		 List<Action> listeActions = Arrays.asList(response2.getBody());
		 model.addAttribute("listeActions", listeActions) ;
	}
	
}
