package cm.deepdream.pointage.server.dao;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Employe;
@Repository
public interface EmployeDAO extends CrudRepository<Employe, Long>{
	public List<Employe> findByEtat (int etat) ;
	public List<Employe> findAll () ;
}
