package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.model.UniteAdministrative;
@Repository
public interface PointageDAO extends CrudRepository<Pointage, Long>{
	@Query("select count(p)>0 from Pointage p where p.employe=:employe and p.datePointage=:datePointage")
	public boolean existsByEmployeAndDatePointage(@Param("employe") Employe employe, @Param("datePointage") LocalDate datePointage) ;
	@Query("select count(p)>0 from Pointage p where p.employe=:employe and p.datePointage=:datePointage and p.direction=:direction")
	public boolean existsByEmployeAndDatePointageAndDirection(@Param("employe") Employe employe, 
			@Param("datePointage") LocalDate datePointage, @Param("direction") String direction) ;
	public List<Pointage> findByDatePointage (LocalDate datePointage) ;
	public List<Pointage> findByDatePointageBetween (LocalDate date1, LocalDate date2) ;
	public List<Pointage> findByEmployeAndDatePointageBetween (Employe employe, LocalDate date1, LocalDate date2) ;
	public List<Pointage> findByEmployeAndDatePointage (Employe employe, LocalDate date1) ;

}
