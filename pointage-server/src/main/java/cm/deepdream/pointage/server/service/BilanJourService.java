package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.BilanJour;
import cm.deepdream.pointage.server.dao.BilanJourDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class BilanJourService {
	private Logger logger = Logger.getLogger(BilanJourService.class.getName()) ;
	@Autowired
	private BilanJourDAO bilanJourDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public BilanJour creer(BilanJour bilanJour) throws Exception {
		try{
			logger.info("Lancement de la creation d'un bilan journalier") ;	
			bilanJour.setId(sequenceDAO.nextId(BilanJour.class.getName())) ;
			bilanJourDAO.save(bilanJour) ;
			return bilanJour ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(BilanJour bilanJour) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un bilan journalier");
			bilanJourDAO.delete(bilanJour) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public BilanJour rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un bilan journalier");
			BilanJour bilanJour = bilanJourDAO.findById(id).orElseThrow(Exception::new) ;
			return bilanJour ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<BilanJour> rechercher(BilanJour bilanJour) throws Exception {
		try{
			logger.info("Lancement de la recherche des bilans journaliers");
			Iterable<BilanJour> bilanJours = bilanJourDAO.findAll() ;
			List<BilanJour> listeBilanJours = new ArrayList<BilanJour>() ;
			bilanJours.forEach(listeBilanJours::add) ;
			return listeBilanJours ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public BilanJour modifier(BilanJour bilanJour) throws Exception {
		try{
			logger.info("Lancement de la modification d'un bilan journalier");
			bilanJourDAO.save(bilanJour) ;
			return bilanJour ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
