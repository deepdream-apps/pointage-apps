package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.enums.EtatSession;
import cm.deepdream.pointage.model.Session;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.server.dao.SessionDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class SessionService {
	private Logger logger = Logger.getLogger(SessionService.class.getName()) ;
	@Autowired
	private SessionDAO sessionDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Session creer(Session session) throws Exception {
		try{
			logger.info("Lancement de la creation d'une session") ;	
			session.setId(sequenceDAO.nextId(Session.class.getName())) ;
			session.setEtat(EtatSession.EN_COURS.getCode()) ;
			sessionDAO.save(session) ;
			return session ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Session session) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une session");
			sessionDAO.delete(session) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Session rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une session");
			Session session = sessionDAO.findById(id).orElseThrow(Exception::new) ;
			return session ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Session> rechercher(Session session) throws Exception {
		try{
			logger.info("Lancement de la recherche des sessions");
			Iterable<Session> sessions = sessionDAO.findAll() ;
			List<Session> listeSessions = new ArrayList<Session>() ;
			sessions.forEach(listeSessions::add) ;
			return listeSessions ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Session modifier(Session session) throws Exception {
		try{
			logger.info("Lancement de la modification d'une session");
			sessionDAO.save(session) ;
			return session ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Session terminer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la fin d'une session de l'utilisateur "+utilisateur);
			List<Session> listeSessions = sessionDAO.findByUtilisateurAndEtatOrderByDateDebutDesc(utilisateur, EtatSession.EN_COURS.getCode()) ;
			Session sessionCourante = listeSessions.size() > 0 ? listeSessions.get(0) : null ;
			if(sessionCourante != null) {
				sessionCourante.setEtat(EtatSession.TERMINE.getCode()) ;
				sessionCourante.setDateFin(LocalDateTime.now());
				sessionDAO.save(sessionCourante) ;
			}
			return sessionCourante ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
