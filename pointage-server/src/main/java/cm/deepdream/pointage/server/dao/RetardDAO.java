package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import cm.deepdream.pointage.model.Absence;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Retard;
@Repository
public interface RetardDAO extends CrudRepository<Retard, Long>{
	public List<Retard> findByEmployeAndDateRetard (Employe employe, LocalDate dateRetard) ;
	public List<Retard> findByDateRetard (LocalDate dateAbsence) ;
	public List<Retard> findByDateRetardBetween (LocalDate date1, LocalDate date2) ;
}
