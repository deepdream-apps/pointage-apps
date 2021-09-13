package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.Site;
import cm.deepdream.pointage.server.dao.SiteDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class SiteService {
	private Logger logger = Logger.getLogger(SiteService.class.getName()) ;
	@Autowired
	private SiteDAO siteDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public Site creer(Site site) throws Exception {
		try{
			logger.info("Lancement de la creation d'un site") ;	
			site.setId(sequenceDAO.nextId(Site.class.getName())) ;
			siteDAO.save(site) ;
			return site ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Site site) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un site");
			siteDAO.delete(site) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Site rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un site");
			Site site = siteDAO.findById(id).orElseThrow(Exception::new) ;
			return site ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Site> rechercher(Site site) throws Exception {
		try{
			logger.info("Lancement de la recherche des sites");
			Iterable<Site> sites = siteDAO.findAll() ;
			List<Site> listeSites = new ArrayList<Site>() ;
			sites.forEach(listeSites::add) ;
			return listeSites ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Site modifier(Site site) throws Exception {
		try{
			logger.info("Lancement de la modification d'un site");
			siteDAO.save(site) ;
			return site ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
