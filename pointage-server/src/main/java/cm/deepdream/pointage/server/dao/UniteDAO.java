package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.UniteAdministrative;
@Repository
public interface UniteDAO extends CrudRepository<UniteAdministrative, Long>{

}
