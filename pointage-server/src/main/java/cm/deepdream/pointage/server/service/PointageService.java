package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.enums.JourSemaine;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Horaire;
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.model.Retard;
import cm.deepdream.pointage.server.dao.EmployeDAO;
import cm.deepdream.pointage.server.dao.HoraireDAO;
import cm.deepdream.pointage.server.dao.PointageDAO;
import cm.deepdream.pointage.server.dao.RetardDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
import cm.deepdream.pointage.server.dao.StatutDAO;
@Transactional
@Service
public class PointageService {
	private Logger logger = Logger.getLogger(PointageService.class.getName()) ;
	@Autowired
	private PointageDAO pointageDAO ;
	@Autowired
	private HoraireDAO horaireDAO ;
	@Autowired
	private EmployeDAO employeDAO ;
	@Autowired
	private RetardDAO retardDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Pointage creer(Pointage pointage) throws Exception {
		try{
			logger.info("Lancement de la creation d'un pointage") ;	
			pointage.setId(sequenceDAO.nextId(Pointage.class.getName())) ;
			Employe employe = employeDAO.findById(pointage.getEmploye().getId()).orElseThrow(Exception::new) ;
			Horaire horaire = horaireDAO.findByStatut(employe.getStatut()) ;
			pointage.setJourSemaine(JourSemaine.getLibelle(pointage.getDatePointage().getDayOfWeek().getValue()));
			pointageDAO.save(pointage) ;
			
			employe.setSite(pointage.getDirection().equals("IN") ? pointage.getSite():null) ;
			employeDAO.save(employe) ;
			
			if(pointage.getHeure().isAfter(horaire.getHeureArrivee()) ) {
				List<Retard> listeRetards = retardDAO.findByEmployeAndDateRetard(pointage.getEmploye(), pointage.getDatePointage()) ;
				//Verifier qu'il s'agit du premier pointage de la journee
				if(listeRetards.size() == 0 && ! pointageDAO.existsByEmployeAndDatePointageAndDirection(employe, pointage.getDatePointage(), "IN")) {
					Retard retard = new Retard() ;
					retard.setId(sequenceDAO.nextId(Retard.class.getName()));
					retard.setEmploye(pointage.getEmploye());
					retard.setDateRetard(pointage.getDatePointage());
					retard.setJourSemaine(JourSemaine.getLibelle(retard.getDateRetard().getDayOfWeek().getValue()));
					retard.setHeureRequise(pointage.getHeureRequise());
					retard.setHeureArrivee(pointage.getHeure());
					Duration duration = Duration.between(pointage.getHeureRequise(), pointage.getHeure()) ;
					LocalTime dureeRetard = LocalTime.of(0, 0, 0);
					dureeRetard = dureeRetard.plusSeconds(duration.getSeconds()) ;
					retard.setDureeRetard(dureeRetard);
					retard.setCreateur(pointage.getCreateur());
					retard.setModificateur(pointage.getModificateur());
					retard.setDateCreation(pointage.getDateCreation());
					retard.setDateDernMaj(pointage.getDateDernMaj());
					retardDAO.save(retard) ;
				}
			}
			return pointage ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Pointage pointage) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un pointage");
			pointageDAO.delete(pointage) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Pointage rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un pointage");
			Pointage pointage = pointageDAO.findById(id).orElseThrow(Exception::new) ;
			return pointage ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Pointage> rechercher(Pointage pointage) throws Exception {
		try{
			logger.info("Lancement de la recherche des pointages");
			Iterable<Pointage> pointages = pointageDAO.findAll() ;
			List<Pointage> listePointages = new ArrayList<Pointage>() ;
			pointages.forEach(listePointages::add) ;
			return listePointages ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Pointage modifier(Pointage pointage) throws Exception {
		try{
			logger.info("Lancement de la modification d'un pointage");
			Employe employe = employeDAO.findById(pointage.getEmploye().getId()).orElseThrow(Exception::new) ;
			Horaire horaire = horaireDAO.findByStatut(employe.getStatut()) ;
			pointage.setHeureRequise(horaire.getHeureArrivee()) ;
			pointage.setJourSemaine(JourSemaine.getLibelle(pointage.getDatePointage().getDayOfWeek().getValue())) ;
			pointageDAO.save(pointage) ;
			return pointage ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Pointage> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des pointages entre "+date1+" et "+date2);
			Iterable<Pointage> pointages = pointageDAO.findByDatePointageBetween(date1, date2);
			List<Pointage> listePointages = new ArrayList<Pointage>() ;
			pointages.forEach(listePointages::add) ;
			return listePointages ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Pointage> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des pointages du "+date);
			Iterable<Pointage> pointages = pointageDAO.findByDatePointage(date) ;
			List<Pointage> listePointages = new ArrayList<Pointage>() ;
			pointages.forEach(listePointages::add) ;
			return listePointages ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
