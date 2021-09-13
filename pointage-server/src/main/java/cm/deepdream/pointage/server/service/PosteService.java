package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Poste;
import cm.deepdream.pointage.server.dao.PosteDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class PosteService {
	private Logger logger = Logger.getLogger(PosteService.class.getName()) ;
	@Autowired
	private PosteDAO posteDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Poste creer(Poste poste) throws Exception {
		try{
			logger.info("Lancement de la creation d'un poste") ;	
			poste.setId(sequenceDAO.nextId(Poste.class.getName())) ;
			posteDAO.save(poste) ;
			return poste ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Poste poste) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un poste");
			posteDAO.delete(poste) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Poste rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un poste");
			Poste poste = posteDAO.findById(id).orElseThrow(Exception::new) ;
			return poste ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Poste> rechercher(Poste poste) throws Exception {
		try{
			logger.info("Lancement de la recherche des postes");
			Iterable<Poste> postes = posteDAO.findAll() ;
			List<Poste> listePostes = new ArrayList<Poste>() ;
			postes.forEach(listePostes::add) ;
			return listePostes ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Poste modifier(Poste poste) throws Exception {
		try{
			logger.info("Lancement de la modification d'un poste");
			posteDAO.save(poste) ;
			return poste ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
