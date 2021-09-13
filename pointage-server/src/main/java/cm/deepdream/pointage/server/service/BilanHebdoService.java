package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.BilanHebdo;
import cm.deepdream.pointage.server.dao.BilanHebdoDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class BilanHebdoService {
	private Logger logger = Logger.getLogger(BilanHebdoService.class.getName()) ;
	@Autowired
	private BilanHebdoDAO bilanHebdoDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public BilanHebdo creer(BilanHebdo bilanHebdo) throws Exception {
		try{
			logger.info("Lancement de la creation d'une bilanHebdo") ;	
			bilanHebdo.setId(sequenceDAO.nextId(BilanHebdo.class.getName())) ;
			bilanHebdoDAO.save(bilanHebdo) ;
			return bilanHebdo ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(BilanHebdo bilanHebdo) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une bilanHebdo");
			bilanHebdoDAO.delete(bilanHebdo) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public BilanHebdo rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'une bilanHebdo");
			BilanHebdo bilanHebdo = bilanHebdoDAO.findById(id).orElseThrow(Exception::new) ;
			return bilanHebdo ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<BilanHebdo> rechercher(BilanHebdo bilanHebdo) throws Exception {
		try{
			logger.info("Lancement de la recherche des bilanHebdos");
			Iterable<BilanHebdo> bilanHebdos = bilanHebdoDAO.findAll() ;
			List<BilanHebdo> listeBilanHebdos = new ArrayList<BilanHebdo>() ;
			bilanHebdos.forEach(listeBilanHebdos::add) ;
			return listeBilanHebdos ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public BilanHebdo modifier(BilanHebdo bilanHebdo) throws Exception {
		try{
			logger.info("Lancement de la modification d'une bilanHebdo");
			bilanHebdoDAO.save(bilanHebdo) ;
			return bilanHebdo ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
