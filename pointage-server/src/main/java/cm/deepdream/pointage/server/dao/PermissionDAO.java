package cm.deepdream.pointage.server.dao;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Permission;
import cm.deepdream.pointage.model.Profil;
@Repository
public interface PermissionDAO extends CrudRepository<Permission, Long>{
	public List<Permission> findByProfil(Profil profil) ;
}
