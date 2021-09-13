package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Visite;
@Repository
public interface VisiteDAO extends CrudRepository<Visite, Long>{
	public List<Visite> findByDateVisite (LocalDate dateAbsence) ;
	public List<Visite> findByDateVisiteBetween (LocalDate date1, LocalDate date2) ;
	public List<Visite> findByTypeIdAndNumeroId (String typeId, String numeroId) ;
}
