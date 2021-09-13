package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Horaire;
import cm.deepdream.pointage.model.Statut;
import cm.deepdream.pointage.server.dao.HoraireDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
import cm.deepdream.pointage.server.dao.StatutDAO;
@Service
public class HoraireService {
	private Logger logger = Logger.getLogger(HoraireService.class.getName()) ;
	@Autowired
	private HoraireDAO horaireDAO ;
	@Autowired
	private StatutDAO statutDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Horaire creer(Horaire horaire) throws Exception {
		try{
			logger.info("Lancement de la creation d'un horaire") ;	
			horaire.setId(sequenceDAO.nextId(Horaire.class.getName())) ;
			horaireDAO.save(horaire) ;
			return horaire ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Horaire horaire) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un horaire");
			horaireDAO.delete(horaire) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Horaire rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un horaire");
			Horaire horaire = horaireDAO.findById(id).orElseThrow(Exception::new) ;
			return horaire ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Horaire rechercherStatut(long idStatut) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un horaire");
			Statut statut = statutDAO.findById(idStatut).orElseThrow(Exception::new) ;
			Horaire horaire = horaireDAO.findByStatut(statut) ;
			return horaire ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Horaire> rechercher(Horaire horaire) throws Exception {
		try{
			logger.info("Lancement de la recherche des horaires");
			Iterable<Horaire> horaires = horaireDAO.findAll() ;
			List<Horaire> listeHoraires = new ArrayList<Horaire>() ;
			horaires.forEach(listeHoraires::add) ;
			return listeHoraires ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Horaire modifier(Horaire horaire) throws Exception {
		try{
			logger.info("Lancement de la modification d'un horaire");
			horaireDAO.save(horaire) ;
			return horaire ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
