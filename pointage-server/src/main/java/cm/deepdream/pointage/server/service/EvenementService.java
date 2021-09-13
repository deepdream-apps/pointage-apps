package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Evenement;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.server.dao.EvenementDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class EvenementService {
	private Logger logger = Logger.getLogger(EvenementService.class.getName()) ;
	@Autowired
	private EvenementDAO evenementDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Evenement creer(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la creation d'un evenement") ;	
			evenement.setId(sequenceDAO.nextId(Evenement.class.getName())) ;
			evenementDAO.save(evenement) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un evenement");
			evenementDAO.delete(evenement) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Evenement rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un evenement");
			Evenement evenement = evenementDAO.findById(id).orElseThrow(Exception::new) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Evenement> rechercher(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la recherche des evenements");
			Iterable<Evenement> evenements = evenementDAO.findAll() ;
			List<Evenement> listeUnites = new ArrayList<Evenement>() ;
			evenements.forEach(listeUnites::add) ;
			return listeUnites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Evenement> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des evenements");
			List<Evenement> evenements = evenementDAO.findByDateEvnmt(date) ;
			return evenements ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Evenement> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des evenements");
			List<Evenement> evenements = evenementDAO.findByDateEvnmtBetween(date1, date2) ;
			return evenements ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Evenement modifier(Evenement evenement) throws Exception {
		try{
			logger.info("Lancement de la modification d'une evenement");
			evenementDAO.save(evenement) ;
			return evenement ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


}
