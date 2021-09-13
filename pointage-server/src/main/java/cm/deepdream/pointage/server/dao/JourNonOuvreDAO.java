package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.JourNonOuvre;
@Repository
public interface JourNonOuvreDAO extends CrudRepository<JourNonOuvre, Long>{

}
