package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.server.dao.StatutDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class StatutService {
	private Logger logger = Logger.getLogger(StatutService.class.getName()) ;
	@Autowired
	private StatutDAO statutDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Statut creer(Statut statut) throws Exception {
		try{
			logger.info("Lancement de la creation d'un statut") ;	
			statut.setId(sequenceDAO.nextId(Statut.class.getName())) ;
			statutDAO.save(statut) ;
			return statut ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Statut statut) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un statut");
			statutDAO.delete(statut) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Statut rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un statut");
			Statut statut = statutDAO.findById(id).orElseThrow(Exception::new) ;
			return statut ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public List<Statut> rechercher(Statut statut) throws Exception {
		try{
			logger.info("Lancement de la recherche des statuts");
			Iterable<Statut> statuts = statutDAO.findAll() ;
			List<Statut> listeStatuts = new ArrayList<Statut>() ;
			statuts.forEach(listeStatuts::add) ;
			return listeStatuts ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public Statut modifier(Statut statut) throws Exception {
		try{
			logger.info("Lancement de la modification d'un statut");
			statutDAO.save(statut) ;
			return statut ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
