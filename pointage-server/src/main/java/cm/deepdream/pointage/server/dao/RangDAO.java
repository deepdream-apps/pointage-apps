package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Rang;
@Repository
public interface RangDAO extends CrudRepository<Rang, Long>{

}
