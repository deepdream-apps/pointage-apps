package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Poste;
@Repository
public interface PosteDAO extends CrudRepository<Poste, Long>{

}
