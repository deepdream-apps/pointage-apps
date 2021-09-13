package cm.deepdream.pointage.server.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.pointage.model.Permission;
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.server.service.PermissionService;
import cm.deepdream.pointage.server.service.ProfilService;

@RestController
@RequestMapping (path = "/ws/permission")
public class PermissionWS {
	private Logger logger = Logger.getLogger(PermissionWS.class.getName()) ;
	@Autowired
	private PermissionService permissionService ;
	@Autowired
	private ProfilService profilService ;
	
	@PostMapping("/ajout")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Permission ajout (@RequestBody Permission permission) {
		try {
			Permission permissionCree = permissionService.creer(permission) ;
			return permissionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Permission maj (@RequestBody Permission permission) {
		try {
			Permission permissionMaj = permissionService.modifier(permission) ;
			return permissionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr/{id}")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (@PathVariable ("id") long id) {
		try {
			Permission permission = permissionService.rechercher(id) ;
			permissionService.supprimer(permission) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/id/{id}")
	public Permission getById (@PathVariable("id") long id) {
		try {
			Permission permission = permissionService.rechercher(id) ;
			return permission ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/profil/{idProfil}")
	public List<Permission> getAll (@PathVariable("idProfil") Long idProfil) {
		try {
			Profil profil = profilService.rechercher(idProfil) ;
			List<Permission> listePermissions = permissionService.rechercher(profil) ;
			return listePermissions ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Permission>() ;
		}
	}
	
	
	@GetMapping("/all")
	public List<Permission> getAll () {
		try {
			Permission permission = new Permission() ;
			List<Permission> listePermissions = permissionService.rechercher(permission) ;
			return listePermissions ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Permission>() ;
		}
	}
}
