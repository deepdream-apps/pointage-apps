package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.BilanMensuel;
@Repository
public interface BilanMensuelDAO extends CrudRepository<BilanMensuel, Long>{

}
