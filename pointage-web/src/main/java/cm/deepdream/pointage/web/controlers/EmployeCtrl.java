package cm.deepdream.pointage.web.controlers;
import java.io.Serializable ;
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

import cm.deepdream.pointage.model.Categorie;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Poste;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.model.Utilisateur;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class EmployeCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EmployeCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	private List<UniteAdministrative> listeUnites = null ;
	private List<Statut> listeStatuts = null ;
	private List<Categorie> listeCategories = null ;
	private List<Poste> listePostes = null ;
	
	@GetMapping ("/personnel/employes")
	public String index (Model model) throws Exception {
		 ResponseEntity<Employe[]> response = rest.getForEntity("http://pointage-zuul/pointage-server/ws/employe/all", Employe[].class) ;
		 List<Employe> listeEmployes = Arrays.asList(response.getBody());
		 model.addAttribute("listeEmployes", listeEmployes) ;
		return "personnel/employes" ;
	}
	

	@GetMapping ("/personnel/ajout-employe")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {		
		 initDependencies(model) ;
		 Employe employe = new Employe() ;
		 employe.setEtat(1);
		 model.addAttribute("employe", employe) ;
		 return "personnel/ajout-employe" ;
	}
	
	@PostMapping ("/personnel/employe/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, Employe employe) throws Exception {
		try {
			employe.setCreateur(utilisateurCourant.getLogin());
			employe.setModificateur(utilisateurCourant.getLogin());
			rest.postForEntity("http://pointage-zuul/pointage-server/ws/employe/ajout", employe, Employe.class);			
			return "redirect:/personnel/employes" ;
		}catch(Exception ex) {
			initDependencies(model) ;
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("employe", employe) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/ajout-employe" ;
		}
	}
	
	@GetMapping ("/personnel/maj-employe/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		initDependencies(model) ;
		 
		Employe employe = rest.getForObject("http://pointage-zuul/pointage-server/ws/employe/id/{id}", Employe.class, id) ;
		model.addAttribute("employeExistant", employe) ;
		
		return "personnel/maj-employe" ;
	}
	
	@PostMapping ("/personnel/employe/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Employe employeExistant) throws Exception {
		try {
			Employe employe = rest.getForObject("http://pointage-zuul/pointage-server/ws/employe/id/{id}", 
					Employe.class, employeExistant.getId()) ;
			employe.setDateDernMaj(LocalDateTime.now()) ;
			employe.setModificateur(utilisateurCourant.getLogin()) ;
			employe.setCivilite(employeExistant.getCivilite());
			employe.setMatricule(employeExistant.getMatricule());
			employe.setNom(employeExistant.getNom());
			employe.setPrenom(employeExistant.getPrenom());
			employe.setSexe(employeExistant.getSexe());
			employe.setUnite(employeExistant.getUnite());
			employe.setStatut(employeExistant.getStatut());
			employe.setPoste(employeExistant.getPoste());
			employe.setTelephone(employeExistant.getTelephone());
			employe.setTelephoneWhatsapp(employeExistant.getTelephoneWhatsapp());
			employe.setEmail(employeExistant.getEmail());
			employe.setLangue(employeExistant.getLangue());
			rest.put("http://pointage-zuul/pointage-server/ws/employe/maj", employe, Employe.class);
			return "redirect:/personnel/employes" ;
		}catch(Exception ex) {
			initDependencies(model) ;
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("employeExistant", employeExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "redirect:personnel/maj-employe/"+employeExistant.getId() ;
		}
	}
	
	
	@GetMapping ("/personnel/details-employe/{id}")
	public String initDetails (Model model, @PathVariable("id") long id) throws Exception {
		Employe employe = rest.getForObject("http://pointage-zuul/pointage-server/ws/employe/id/{id}", 
				Employe.class, id) ;
		model.addAttribute("employeExistant", employe) ;
		return "personnel/details-employe" ;
	}
	
	
	@PostMapping ("/personnel/employe/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Employe employeExistant) throws Exception {
		try {
			Employe employe = rest.getForObject("http://pointage-zuul/pointage-server/ws/employe/id/{id}", 
					Employe.class, employeExistant.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", employe.getId()) ;
			rest.delete("http://pointage-zuul/pointage-server/ws/employe/suppr/{id}", uriVariables);
			return "redirect:/personnel/employes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("employeExistant", employeExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "personnel/details-employe" ;
		}
	}
	
	private void initDependencies (Model model) {
		
		 ResponseEntity<Statut[]> response2 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/statut/all", Statut[].class) ;
		 List<Statut> listeStatuts = Arrays.asList(response2.getBody());
		 model.addAttribute("listeStatuts", listeStatuts) ;
		 
		 ResponseEntity<Poste[]> response3 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/poste/all", Poste[].class) ;
		 List<Poste> listePostes = Arrays.asList(response3.getBody());
		 model.addAttribute("listePostes", listePostes) ;
		 
		 ResponseEntity<UniteAdministrative[]> response4 = rest.getForEntity("http://pointage-zuul/pointage-server/ws/unite/all", UniteAdministrative[].class) ;
		 List<UniteAdministrative> listeUnites = Arrays.asList(response4.getBody());
		 model.addAttribute("listeUnites", listeUnites) ;
	}
	
	
}
