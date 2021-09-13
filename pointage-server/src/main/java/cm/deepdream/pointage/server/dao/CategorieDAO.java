package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Categorie;
@Repository
public interface CategorieDAO extends CrudRepository<Categorie, Long>{

}
