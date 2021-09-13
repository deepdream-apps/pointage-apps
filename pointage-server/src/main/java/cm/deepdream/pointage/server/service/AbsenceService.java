package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.enums.Booleen;
import cm.deepdream.pointage.enums.JourSemaine;
import cm.deepdream.pointage.model.Absence;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.EnregistrementAbsence;
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.server.dao.AbsenceDAO;
import cm.deepdream.pointage.server.dao.EmployeDAO;
import cm.deepdream.pointage.server.dao.EnregistrementAbsenceDAO;
import cm.deepdream.pointage.server.dao.PointageDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class AbsenceService {
	private Logger logger = Logger.getLogger(AbsenceService.class.getName()) ;
	@Autowired
	private AbsenceDAO absenceDAO ;
	@Autowired
	private PointageDAO pointageDAO ;
	@Autowired
	private EmployeDAO employeDAO ;
	@Autowired
	private EnregistrementAbsenceDAO enregistrementDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Absence creer(Absence absence) throws Exception {
		try{
			logger.info("Lancement de la creation d'une absence") ;	
			absence.setId(sequenceDAO.nextId(Absence.class.getName())) ;
			absence.setLibelleJustifiee(Booleen.getLibelle(absence.getJustifiee()));
			absence.setJourSemaine(JourSemaine.getLibelle(absence.getDateAbsence().getDayOfWeek().getValue()));
			absenceDAO.save(absence) ;
			return absence ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Absence absence) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une absence");
			absenceDAO.delete(absence) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Absence rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une absence");
			Absence absence = absenceDAO.findById(id).orElseThrow(Exception::new) ;
			return absence ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Absence> rechercher(Absence absence) throws Exception {
		try{
			logger.info("Lancement de la recherche des absences");
			Iterable<Absence> absences = absenceDAO.findAll() ;
			List<Absence> listeAbsences = new ArrayList<Absence>() ;
			absences.forEach(listeAbsences::add) ;
			return listeAbsences ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Absence modifier(Absence absence) throws Exception {
		try{
			logger.info("Lancement de la modification d'une absence") ;
			absence.setLibelleJustifiee(Booleen.getLibelle(absence.getJustifiee()));
			absence.setJourSemaine(JourSemaine.getLibelle(absence.getDateAbsence().getDayOfWeek().getValue()));
			absenceDAO.save(absence) ;
			return absence ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void enregistrerAbsences(LocalDate dateJour) throws Exception {
		try{
			EnregistrementAbsence enregAbsence = enregistrementDAO.findFirstByOrderByDateEnregDesc() ;
			LocalDate lastEnreg = (enregAbsence == null ||enregAbsence.getDateEnreg() == null) ? LocalDate.now() : enregAbsence.getDateEnreg() ;
			List<Employe> listeEmployes = employeDAO.findByEtat(1) ;
			logger.info("Nombre d'employes "+listeEmployes.size());
			
			LocalDate now = LocalDate.now() ;
			
			while (! lastEnreg.isAfter(now)) {
				if(! enregistrementDAO.existsByDateEnreg(lastEnreg)) {
					int nbAbsences = 0 ;
					for(Employe employe : listeEmployes) {
						boolean absent =  pointageDAO.existsByEmployeAndDatePointage(employe, lastEnreg) ;
						logger.info("Employe "+employe.getNom()+" est t-il absence ? "+absent) ;;
						if(! absent) {
							logger.info("Employe present  "+employe.getNom());
							Absence absence = new Absence() ;
							absence.setId(sequenceDAO.nextId(Absence.class.getName())) ;
							absence.setEmploye(employe) ;
							absence.setDateAbsence(dateJour) ;
							absence.setJourSemaine(JourSemaine.getLibelle(absence.getDateAbsence().getDayOfWeek().getValue())) ;
							absence.setJustifiee(0) ;
							absence.setLibelleJustifiee(Booleen.getLibelle(absence.getJustifiee()));
							absenceDAO.save(absence) ;
							nbAbsences ++ ;
						}
					}
					EnregistrementAbsence enregAbs = new EnregistrementAbsence() ;
					enregAbs.setId(sequenceDAO.nextId(EnregistrementAbsence.class.getName())) ;
					enregAbs.setDateEnreg(lastEnreg) ;
					enregAbs.setNbAbsences(nbAbsences) ;
					enregistrementDAO.save(enregAbs) ;
				}
				lastEnreg = lastEnreg.plusDays(1L) ;
			}
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Absence> rechercher(LocalDate date1, LocalDate date2) throws Exception {
		try{
			logger.info("Lancement de la recherche des absences entre "+date1+" et "+date2);
			List<Absence> listeAbsences = absenceDAO.findByDateAbsenceBetween(date1, date2);
			return listeAbsences ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Absence> rechercher(LocalDate date) throws Exception {
		try{
			logger.info("Lancement de la recherche des absences du "+date);
			List<Absence> listeAbsences= absenceDAO.findByDateAbsence(date) ;
			return listeAbsences ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

}
