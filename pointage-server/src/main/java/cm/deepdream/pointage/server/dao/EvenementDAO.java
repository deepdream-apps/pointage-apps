package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Evenement;
@Repository
public interface EvenementDAO extends CrudRepository<Evenement, Long>{
	public List<Evenement> findByDateEvnmtBetween(LocalDate date1, LocalDate date2) ;
	public List<Evenement> findByDateEvnmt(LocalDate date) ;
}
