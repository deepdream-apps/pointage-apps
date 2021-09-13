package cm.deepdream.pointage.server.dao;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.BilanJour;
@Repository
public interface BilanJourDAO extends CrudRepository<BilanJour, Long>{

}
