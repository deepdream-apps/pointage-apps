package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Visiteur;
import cm.deepdream.pointage.server.dao.VisiteurDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class VisiteurService {
	private Logger logger = Logger.getLogger(VisiteurService.class.getName()) ;
	@Autowired
	private VisiteurDAO visiteurDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Visiteur creer(Visiteur visiteur) throws Exception {
		try{
			logger.info("Lancement de la creation d'une visiteur") ;	
			visiteur.setId(sequenceDAO.nextId(Visiteur.class.getName())) ;
			visiteurDAO.save(visiteur) ;
			return visiteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Visiteur visiteur) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une visiteur");
			visiteurDAO.delete(visiteur) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Visiteur rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une visiteur");
			Visiteur visiteur = visiteurDAO.findById(id).orElseThrow(Exception::new) ;
			return visiteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Visiteur> rechercher(Visiteur visiteur) throws Exception {
		try{
			logger.info("Lancement de la recherche des visiteurs");
			Iterable<Visiteur> visiteurs = visiteurDAO.findAll() ;
			List<Visiteur> listeVisiteurs = new ArrayList<Visiteur>() ;
			visiteurs.forEach(listeVisiteurs::add) ;
			return listeVisiteurs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Visiteur modifier(Visiteur visiteur) throws Exception {
		try{
			logger.info("Lancement de la modification d'une visiteur");
			visiteurDAO.save(visiteur) ;
			return visiteur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visiteur> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des visiteurs entre "+date1+" et "+date2);
			List<Visiteur> listeVisiteurs = visiteurDAO.findByDateDernVisiteBetween(date1, date2);
			return listeVisiteurs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visiteur> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des visiteurs du "+date);
			List<Visiteur> listeVisiteurs= visiteurDAO.findByDateDernVisiteAfter(date) ;
			return listeVisiteurs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visiteur> rechercherVisiteursFrequents(int nombre) throws Exception {
		try{
			logger.info("Lancement de la recherche des visiteurs les plus fréquents "+nombre);
			List<Visiteur> listeVisiteurs= visiteurDAO.findByOrderByNbVisitesDesc() ;
			return listeVisiteurs.subList(0, nombre-1) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
