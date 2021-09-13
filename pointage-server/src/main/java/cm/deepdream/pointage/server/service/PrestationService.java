package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.enums.TypePrest;
import cm.deepdream.pointage.model.Prestation;
import cm.deepdream.pointage.server.dao.PrestationDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class PrestationService {
	private Logger logger = Logger.getLogger(PrestationService.class.getName()) ;
	@Autowired
	private PrestationDAO prestationDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Prestation creer(Prestation prestation) throws Exception {
		try{
			logger.info("Lancement de la creation d'une prestation") ;	
			prestation.setId(sequenceDAO.nextId(Prestation.class.getName())) ;
			prestation.setLibelleType(TypePrest.getLibelle(prestation.getType()));
			prestationDAO.save(prestation) ;
			return prestation ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Prestation prestation) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une prestation");
			prestationDAO.delete(prestation) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Prestation rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une prestation");
			Prestation prestation = prestationDAO.findById(id).orElseThrow(Exception::new) ;
			return prestation ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Prestation> rechercher(Prestation prestation) throws Exception {
		try{
			logger.info("Lancement de la recherche des prestations");
			Iterable<Prestation> prestations = prestationDAO.findAll() ;
			List<Prestation> listePrestations = new ArrayList<Prestation>() ;
			prestations.forEach(listePrestations::add) ;
			return listePrestations ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Prestation modifier(Prestation prestation) throws Exception {
		try{
			logger.info("Lancement de la modification d'une prestation");
			prestation.setLibelleType(TypePrest.getLibelle(prestation.getType()));
			prestationDAO.save(prestation) ;
			return prestation ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Prestation> rechercher(int type) throws Exception {
		try{
			logger.info("Lancement de la recherche des prestations de type "+TypePrest.getLibelle(type));
			List<Prestation> listePrestations = prestationDAO.findByType(type) ;
			return listePrestations ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
