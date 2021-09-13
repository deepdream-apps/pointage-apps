package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Statut;
@Repository
public interface StatutDAO extends CrudRepository<Statut, Long>{

}
