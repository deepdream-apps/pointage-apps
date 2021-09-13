package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Utilisateur;
@Repository
public interface UtilisateurDAO extends CrudRepository<Utilisateur, Long>{
	public Utilisateur findByLogin(String login) ;
}
