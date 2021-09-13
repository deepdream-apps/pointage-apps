package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.server.dao.UniteDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class UniteService {
	private Logger logger = Logger.getLogger(UniteService.class.getName()) ;
	@Autowired
	private UniteDAO uniteDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public UniteAdministrative creer(UniteAdministrative unite) throws Exception {
		try{
			logger.info("Lancement de la creation d'une unite") ;	
			unite.setId(sequenceDAO.nextId(UniteAdministrative.class.getName())) ;
			uniteDAO.save(unite) ;
			return unite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(UniteAdministrative unite) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une unite");
			uniteDAO.delete(unite) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public UniteAdministrative rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une unite");
			UniteAdministrative unite = uniteDAO.findById(id).orElseThrow(Exception::new) ;
			return unite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<UniteAdministrative> rechercher(UniteAdministrative unite) throws Exception {
		try{
			logger.info("Lancement de la recherche des unites");
			Iterable<UniteAdministrative> unites = uniteDAO.findAll() ;
			List<UniteAdministrative> listeUnites = new ArrayList<UniteAdministrative>() ;
			unites.forEach(listeUnites::add) ;
			return listeUnites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public UniteAdministrative modifier(UniteAdministrative unite) throws Exception {
		try{
			logger.info("Lancement de la modification d'une unite");
			uniteDAO.save(unite) ;
			return unite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
