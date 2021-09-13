package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Site;

@Repository
public interface SiteDAO extends CrudRepository<Site, Long>{

}
