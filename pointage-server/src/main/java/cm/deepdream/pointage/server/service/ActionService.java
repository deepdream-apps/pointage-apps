package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Action;
import cm.deepdream.pointage.server.dao.ActionDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class ActionService {
	private Logger logger = Logger.getLogger(ActionService.class.getName()) ;
	@Autowired
	private ActionDAO actionDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Action creer(Action action) throws Exception {
		try{
			logger.info("Lancement de la creation d'une action") ;	
			action.setId(sequenceDAO.nextId(Action.class.getName())) ;
			actionDAO.save(action) ;
			return action ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Action action) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une action");
			actionDAO.delete(action) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Action rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une action");
			Action action = actionDAO.findById(id).orElseThrow(Exception::new) ;
			return action ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Action> rechercher(Action action) throws Exception {
		try{
			logger.info("Lancement de la recherche des actions");
			Iterable<Action> actions = actionDAO.findAll() ;
			List<Action> listeUnites = new ArrayList<Action>() ;
			actions.forEach(listeUnites::add) ;
			return listeUnites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Action modifier(Action action) throws Exception {
		try{
			logger.info("Lancement de la modification d'une action");
			actionDAO.save(action) ;
			return action ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
