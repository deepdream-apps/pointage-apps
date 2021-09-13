package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.enums.JourSemaine;
import cm.deepdream.pointage.enums.MotifVisite;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.model.Visiteur;
import cm.deepdream.pointage.model.Visite;
import cm.deepdream.pointage.server.dao.VisiteDAO;
import cm.deepdream.pointage.server.dao.VisiteurDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class VisiteService {
	private Logger logger = Logger.getLogger(VisiteService.class.getName()) ;
	@Autowired
	private VisiteDAO visiteDAO ;
	@Autowired
	private VisiteurDAO visiteurDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Visite creer(Visite visite) throws Exception {
		try{
			logger.info("Lancement de la creation d'une visite") ;	
			visite.setId(sequenceDAO.nextId(Visite.class.getName())) ;
			visite.setJourSemaine(JourSemaine.getLibelle(visite.getDateVisite().getDayOfWeek().getValue()));
			visite.setLibelleMotif(MotifVisite.getLibelle(visite.getMotif()));
			visiteDAO.save(visite) ;
			if(visiteurDAO.existsByTypeIdAndNumeroId(visite.getTypeId(), visite.getNumeroId())) {
				Visiteur visiteur = visiteurDAO.findByTypeIdAndNumeroId(visite.getTypeId(), visite.getNumeroId()) ;
				visiteur.setDateDernVisite(visite.getDateVisite());
				visiteur.setNbVisites(visiteur.getNbVisites()+1);
				visiteurDAO.save(visiteur) ;
			}else {
				Visiteur visiteur = new Visiteur() ;
				visiteur.setId(sequenceDAO.nextId(Visiteur.class.getName())) ;
				visiteur.setTypeId(visite.getTypeId());
				visiteur.setNumeroId(visite.getNumeroId());
				visiteur.setDatePi(visite.getDatePi());
				visiteur.setNom(visite.getNom());
				visiteur.setPays(visite.getPays());
				visiteur.setPhoto(visite.getPhoto());
				visiteur.setNumeroWhatsapp(visite.getNumeroWhatsapp());
				visiteur.setEmail(visite.getEmail());
				visiteur.setNbVisites(1);
				visiteur.setDateDernVisite(visite.getDateVisite());
				visiteurDAO.save(visiteur) ;
			}
			return visite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Visite visite) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une visite");
			visiteDAO.delete(visite) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Visite rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une visite");
			Visite visite = visiteDAO.findById(id).orElseThrow(Exception::new) ;
			return visite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Visite> rechercher(Visite visite) throws Exception {
		try{
			logger.info("Lancement de la recherche des visites");
			Iterable<Visite> visites = visiteDAO.findAll() ;
			List<Visite> listeVisites = new ArrayList<Visite>() ;
			visites.forEach(listeVisites::add) ;
			return listeVisites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Visite modifier(Visite visite) throws Exception {
		try{
			logger.info("Lancement de la modification d'une visite");
			visite.setJourSemaine(JourSemaine.getLibelle(visite.getDateVisite().getDayOfWeek().getValue()));
			visite.setLibelleMotif(MotifVisite.getLibelle(visite.getMotif()));
			visiteDAO.save(visite) ;
			return visite ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visite> rechercher(String typeId, String numeroId) throws Exception {
		try{
			logger.info("Lancement de la recherche des visites entre "+typeId+" et "+numeroId);
			List<Visite> visites = visiteDAO.findByTypeIdAndNumeroId(typeId, numeroId);
			return visites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visite> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des visites entre "+date1+" et "+date2);
			List<Visite> listeVisites = visiteDAO.findByDateVisiteBetween(date1, date2);
			return listeVisites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Visite> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des visites du "+date);
			List<Visite> listeVisites= visiteDAO.findByDateVisite(date) ;
			return listeVisites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
