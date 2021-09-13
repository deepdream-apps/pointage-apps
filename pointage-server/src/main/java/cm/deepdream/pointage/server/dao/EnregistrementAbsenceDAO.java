package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.EnregistrementAbsence;
@Repository
public interface EnregistrementAbsenceDAO extends CrudRepository<EnregistrementAbsence, Long>{
	public EnregistrementAbsence findFirstByOrderByDateEnregDesc();
	@Query("select count(e)>0 from EnregistrementAbsence e where e.dateEnreg=:dateEnreg")
	public boolean existsByDateEnreg(@Param("dateEnreg") LocalDate dateEnreg) ;
}
