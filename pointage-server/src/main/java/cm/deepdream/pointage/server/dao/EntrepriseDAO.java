package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Entreprise;
@Repository
public interface EntrepriseDAO extends CrudRepository<Entreprise, Long>{

}
