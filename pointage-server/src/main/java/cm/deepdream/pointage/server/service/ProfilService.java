package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Profil;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.server.dao.ProfilDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
import cm.deepdream.pointage.server.dao.StatutDAO;
@Service
public class ProfilService {
	private Logger logger = Logger.getLogger(ProfilService.class.getName()) ;
	@Autowired
	private ProfilDAO profilDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Profil creer(Profil profil) throws Exception {
		try{
			logger.info("Lancement de la creation d'un profil") ;	
			profil.setId(sequenceDAO.nextId(Profil.class.getName())) ;
			profilDAO.save(profil) ;
			return profil ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Profil profil) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un profil");
			profilDAO.delete(profil) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Profil rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un profil");
			Profil profil = profilDAO.findById(id).orElseThrow(Exception::new) ;
			return profil ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Profil> rechercher(Profil profil) throws Exception {
		try{
			logger.info("Lancement de la recherche des profils");
			Iterable<Profil> profils = profilDAO.findAll() ;
			List<Profil> listeProfils = new ArrayList<Profil>() ;
			profils.forEach(listeProfils::add) ;
			return listeProfils ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Profil modifier(Profil profil) throws Exception {
		try{
			logger.info("Lancement de la modification d'un profil");
			profilDAO.save(profil) ;
			return profil ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
