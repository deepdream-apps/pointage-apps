package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Absence;

@Repository
public interface AbsenceDAO extends CrudRepository<Absence, Long>{
	public List<Absence> findByDateAbsence (LocalDate dateAbsence) ;
	public List<Absence> findByDateAbsenceBetween (LocalDate date1, LocalDate date2) ;
}
