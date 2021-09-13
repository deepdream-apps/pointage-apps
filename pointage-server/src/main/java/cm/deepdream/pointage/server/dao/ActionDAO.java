package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.pointage.model.Action;

@Repository
public interface ActionDAO extends CrudRepository<Action, Long>{

}
