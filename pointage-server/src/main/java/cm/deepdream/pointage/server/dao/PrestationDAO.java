package cm.deepdream.pointage.server.dao;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Prestation;
@Repository
public interface PrestationDAO extends CrudRepository<Prestation, Long>{
	public List<Prestation> findByType (int type) ;
}
