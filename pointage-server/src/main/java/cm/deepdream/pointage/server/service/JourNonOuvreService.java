package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.JourNonOuvre;
import cm.deepdream.pointage.server.dao.JourNonOuvreDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class JourNonOuvreService {
	private Logger logger = Logger.getLogger(JourNonOuvreService.class.getName()) ;
	@Autowired
	private JourNonOuvreDAO jourNonOuvreDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public JourNonOuvre creer(JourNonOuvre jourNonOuvre) throws Exception {
		try{
			logger.info("Lancement de la creation d'un jour non ouvré") ;	
			jourNonOuvre.setId(sequenceDAO.nextId(JourNonOuvre.class.getName())) ;
			jourNonOuvreDAO.save(jourNonOuvre) ;
			return jourNonOuvre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(JourNonOuvre jourNonOuvre) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un jour non ouvré");
			jourNonOuvreDAO.delete(jourNonOuvre) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public JourNonOuvre rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un jour non ouvré");
			JourNonOuvre jourNonOuvre = jourNonOuvreDAO.findById(id).orElseThrow(Exception::new) ;
			return jourNonOuvre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<JourNonOuvre> rechercher(JourNonOuvre jourNonOuvre) throws Exception {
		try{
			logger.info("Lancement de la recherche des jours non ouvrés");
			Iterable<JourNonOuvre> jourNonOuvres = jourNonOuvreDAO.findAll() ;
			List<JourNonOuvre> listeJourNonOuvres = new ArrayList<JourNonOuvre>() ;
			jourNonOuvres.forEach(listeJourNonOuvres::add) ;
			return listeJourNonOuvres ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public JourNonOuvre modifier(JourNonOuvre jourNonOuvre) throws Exception {
		try{
			logger.info("Lancement de la modification d'un jour non ouvré");
			jourNonOuvreDAO.save(jourNonOuvre) ;
			return jourNonOuvre ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public boolean jourNonOuvre(LocalDate date) throws Exception {
		try{
			logger.info("Lancement du test sur "+date+" jour ouvre ?");
			Iterable<JourNonOuvre> listeJours = jourNonOuvreDAO.findAll() ;
			for (JourNonOuvre jour : listeJours) {
				String jourMois = (jour.getJourMois() == null || jour.getJourMois().equals("")) ? date.format(DateTimeFormatter.ofPattern("dd")) : jour.getJourMois() ;
				String mois = (jour.getMois() == null || jour.getMois().equals("")) ? date.format(DateTimeFormatter.ofPattern("MM")) : jour.getMois() ;
				String annee = (jour.getAnnee() == null || jour.getAnnee().equals("")) ? date.format(DateTimeFormatter.ofPattern("yyyy")) : jour.getAnnee() ;
				
				LocalDate jourNonOuvre = LocalDate.of(Integer.parseInt(annee), Integer.parseInt(mois), Integer.parseInt(jourMois)) ;
				
				String jourSemaine = Integer.toString(jourNonOuvre.getDayOfWeek().getValue()) ;
				
				if(jourNonOuvre.isEqual(date) && (jour.getJourSemaine() == null || Integer.parseInt(jour.getJourSemaine()) == date.getDayOfWeek().getValue())) {
					return true ;
				}
			}
			return false ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
