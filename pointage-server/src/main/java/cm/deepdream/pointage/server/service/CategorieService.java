package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Categorie;
import cm.deepdream.pointage.server.dao.CategorieDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class CategorieService {
	private Logger logger = Logger.getLogger(CategorieService.class.getName()) ;
	@Autowired
	private CategorieDAO categorieDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Categorie creer(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la creation d'une categorie") ;	
			categorie.setId(sequenceDAO.nextId(Categorie.class.getName())) ;
			categorieDAO.save(categorie) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une categorie");
			categorieDAO.delete(categorie) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Categorie rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une categorie");
			Categorie categorie = categorieDAO.findById(id).orElseThrow(Exception::new) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Categorie> rechercher(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la recherche des categories");
			Iterable<Categorie> categories = categorieDAO.findAll() ;
			List<Categorie> listeUnites = new ArrayList<Categorie>() ;
			categories.forEach(listeUnites::add) ;
			return listeUnites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Categorie modifier(Categorie categorie) throws Exception {
		try{
			logger.info("Lancement de la modification d'une categorie");
			categorieDAO.save(categorie) ;
			return categorie ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
