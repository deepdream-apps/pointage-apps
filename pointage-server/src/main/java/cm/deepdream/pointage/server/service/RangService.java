package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Rang;
import cm.deepdream.pointage.server.dao.RangDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class RangService {
	private Logger logger = Logger.getLogger(RangService.class.getName()) ;
	@Autowired
	private RangDAO rangDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Rang creer(Rang rang) throws Exception {
		try{
			logger.info("Lancement de la creation d'un rang") ;	
			rang.setId(sequenceDAO.nextId(Rang.class.getName())) ;
			rangDAO.save(rang) ;
			return rang ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Rang rang) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un rang");
			rangDAO.delete(rang) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Rang rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un rang");
			Rang rang = rangDAO.findById(id).orElseThrow(Exception::new) ;
			return rang ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Rang> rechercher(Rang rang) throws Exception {
		try{
			logger.info("Lancement de la recherche des rangs");
			Iterable<Rang> rangs = rangDAO.findAll() ;
			List<Rang> listeRangs = new ArrayList<Rang>() ;
			rangs.forEach(listeRangs::add) ;
			return listeRangs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Rang modifier(Rang rang) throws Exception {
		try{
			logger.info("Lancement de la modification d'un rang");
			rangDAO.save(rang) ;
			return rang ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
