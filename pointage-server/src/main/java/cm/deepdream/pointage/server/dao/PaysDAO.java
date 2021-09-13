package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Pays;

@Repository
public interface PaysDAO extends CrudRepository<Pays, Long>{

}
