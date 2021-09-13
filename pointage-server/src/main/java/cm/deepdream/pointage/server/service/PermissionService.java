package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Permission;
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.server.dao.PermissionDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
import cm.deepdream.pointage.server.dao.StatutDAO;
@Service
public class PermissionService {
	private Logger logger = Logger.getLogger(PermissionService.class.getName()) ;
	@Autowired
	private PermissionDAO permissionDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Permission creer(Permission permission) throws Exception {
		try{
			logger.info("Lancement de la creation d'une permission") ;	
			permission.setId(sequenceDAO.nextId(Permission.class.getName())) ;
			permissionDAO.save(permission) ;
			return permission ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Permission permission) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une permission");
			permissionDAO.delete(permission) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Permission rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une permission");
			Permission permission = permissionDAO.findById(id).orElseThrow(Exception::new) ;
			return permission ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Permission> rechercher(Permission permission) throws Exception {
		try{
			logger.info("Lancement de la recherche des permissions");
			Iterable<Permission> permissions = permissionDAO.findAll() ;
			List<Permission> listePermissions = new ArrayList<Permission>() ;
			permissions.forEach(listePermissions::add) ;
			return listePermissions ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Permission> rechercher(Profil profil) throws Exception {
		try{
			logger.info("Lancement de la recherche des permissions d'un profil");
			List<Permission> listePermissions = permissionDAO.findByProfil(profil) ;
			return listePermissions ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Permission modifier(Permission permission) throws Exception {
		try{
			logger.info("Lancement de la modification d'une permission");
			permissionDAO.save(permission) ;
			return permission ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
