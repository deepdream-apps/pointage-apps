package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Pays;
import cm.deepdream.pointage.server.dao.PaysDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class PaysService {
	private Logger logger = Logger.getLogger(PaysService.class.getName()) ;
	@Autowired
	private PaysDAO paysDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Pays creer(Pays pays) throws Exception {
		try{
			logger.info("Lancement de la creation d'un pays") ;	
			pays.setId(sequenceDAO.nextId(Pays.class.getName())) ;
			paysDAO.save(pays) ;
			return pays ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Pays pays) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un pays");
			paysDAO.delete(pays) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Pays rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un pays");
			Pays pays = paysDAO.findById(id).orElseThrow(Exception::new) ;
			return pays ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Pays> rechercher(Pays pays) throws Exception {
		try{
			logger.info("Lancement de la recherche des payss");
			Iterable<Pays> payss = paysDAO.findAll() ;
			List<Pays> listePayss = new ArrayList<Pays>() ;
			payss.forEach(listePayss::add) ;
			return listePayss ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Pays modifier(Pays pays) throws Exception {
		try{
			logger.info("Lancement de la modification d'un pays");
			paysDAO.save(pays) ;
			return pays ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
