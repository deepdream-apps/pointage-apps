package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cm.deepdream.pointage.model.BilanMensuel;
import cm.deepdream.pointage.server.dao.BilanMensuelDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
@Service
public class BilanMensuelService {
	private Logger logger = Logger.getLogger(BilanMensuelService.class.getName()) ;
	@Autowired
	private BilanMensuelDAO bilanMensuelDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	public BilanMensuel creer(BilanMensuel bilanMensuel) throws Exception {
		try{
			logger.info("Lancement de la creation d'une bilan mensuel") ;	
			bilanMensuel.setId(sequenceDAO.nextId(BilanMensuel.class.getName())) ;
			bilanMensuelDAO.save(bilanMensuel) ;
			return bilanMensuel ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(BilanMensuel bilanMensuel) throws Exception {
		try{
			logger.info("Lancement de la suppression d'une bilan mensuel");
			bilanMensuelDAO.delete(bilanMensuel) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public BilanMensuel rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un bilan mensuel");
			BilanMensuel bilanMensuel = bilanMensuelDAO.findById(id).orElseThrow(Exception::new) ;
			return bilanMensuel ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<BilanMensuel> rechercher(BilanMensuel bilanMensuel) throws Exception {
		try{
			logger.info("Lancement de la recherche des bilans mensuels");
			Iterable<BilanMensuel> bilanMensuels = bilanMensuelDAO.findAll() ;
			List<BilanMensuel> listeBilanMensuels = new ArrayList<BilanMensuel>() ;
			bilanMensuels.forEach(listeBilanMensuels::add) ;
			return listeBilanMensuels ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public BilanMensuel modifier(BilanMensuel bilanMensuel) throws Exception {
		try{
			logger.info("Lancement de la modification d'un bilan mensuel");
			bilanMensuelDAO.save(bilanMensuel) ;
			return bilanMensuel ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
