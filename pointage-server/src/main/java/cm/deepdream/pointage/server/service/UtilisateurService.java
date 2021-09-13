package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Utilisateur;
import cm.deepdream.pointage.server.dao.UtilisateurDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class UtilisateurService {
	private Logger logger = Logger.getLogger(UtilisateurService.class.getName()) ;
	@Autowired
	private UtilisateurDAO utilisateurDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private Environment env ;

	public Utilisateur creer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la creation d'un utilisateur") ;	
			utilisateur.setDateCreation(LocalDateTime.now());
			utilisateur.setDateDernMaj(LocalDateTime.now());
			utilisateur.setDateExpirationMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
			utilisateur.setDateExpiration(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
			utilisateur.setId(sequenceDAO.nextId(Utilisateur.class.getName())) ;
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un utilisateur");
			utilisateurDAO.delete(utilisateur) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Utilisateur rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un utilisateur");
			Utilisateur utilisateur = utilisateurDAO.findById(id).orElseThrow(Exception::new) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Utilisateur rechercher(String login) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un utilisateur");
			Utilisateur utilisateur = utilisateurDAO.findByLogin(login);
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public List<Utilisateur> rechercher(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la recherche des utilisateurs");
			Iterable<Utilisateur> utilisateurs = utilisateurDAO.findAll() ;
			List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>() ;
			utilisateurs.forEach(listeUtilisateurs::add) ;
			return listeUtilisateurs ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Utilisateur modifier(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de la modification d'un utilisateur");
			utilisateur.setDateDernMaj(LocalDateTime.now());
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Utilisateur activer(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de l'activation d'un utilisateur");
			//if(! EtatUtilisateur.ACTIVE.getCode().equals(utilisateur.getEtat())) {
				utilisateur.setEtat(1);
				utilisateur.setDateExpiration(utilisateur.getDateExpiration().plusMonths(Integer.parseInt(env.getProperty("app.security.account_validity_period"))));
				utilisateurDAO.save(utilisateur) ;
			/*} else {
				throw new RhException("msg_compte_deja_active") ;
			}*/
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Utilisateur desactiver(Utilisateur utilisateur) throws Exception {
		try{
			logger.info("Lancement de l'activation d'un utilisateur");
			//if(! EtatUtilisateur.ACTIVE.getCode().equals(utilisateur.getEtat())) {
				utilisateur.setEtat(4);
				utilisateur.setDateExpiration(utilisateur.getDateExpiration().plusMonths(Integer.parseInt(env.getProperty("app.security.account_validity_period"))));
				utilisateurDAO.save(utilisateur) ;
			/*} else {
				throw new RhException("msg_compte_deja_active") ;
			}*/
			return utilisateur ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
