package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.model.Absence;
import cm.deepdream.pointage.model.Retard;
import cm.deepdream.pointage.server.dao.RetardDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class RetardService {
	private Logger logger = Logger.getLogger(RetardService.class.getName()) ;
	@Autowired
	private RetardDAO retardDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Retard creer(Retard retard) throws Exception {
		try{
			logger.info("Lancement de la creation d'un retard") ;	
			retard.setId(sequenceDAO.nextId(Retard.class.getName())) ;
			retardDAO.save(retard) ;
			return retard ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Retard retard) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un retard");
			retardDAO.delete(retard) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Retard rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un retard");
			Retard retard = retardDAO.findById(id).orElseThrow(Exception::new) ;
			return retard ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Retard> rechercher(Retard retard) throws Exception {
		try{
			logger.info("Lancement de la recherche des retards");
			Iterable<Retard> retards = retardDAO.findAll() ;
			List<Retard> listeRetards = new ArrayList<Retard>() ;
			retards.forEach(listeRetards::add) ;
			return listeRetards ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Retard modifier(Retard retard) throws Exception {
		try{
			logger.info("Lancement de la modification d'un retard");
			retardDAO.save(retard) ;
			return retard ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Retard> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des retards entre "+date1+" et "+date2);
			List<Retard> listeRetards = retardDAO.findByDateRetardBetween(date1, date2);
			return listeRetards ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Retard> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des retards du "+date);
			List<Retard> listeRetards= retardDAO.findByDateRetard(date) ;
			return listeRetards ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
