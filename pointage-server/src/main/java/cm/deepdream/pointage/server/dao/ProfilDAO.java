package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Profil;
@Repository
public interface ProfilDAO extends CrudRepository<Profil, Long>{
}
