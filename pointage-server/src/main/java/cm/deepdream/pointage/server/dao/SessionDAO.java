package cm.deepdream.pointage.server.dao;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Session;
import cm.deepdream.pointage.model.Utilisateur;
@Repository
public interface SessionDAO extends CrudRepository<Session, Long>{
	public List<Session> findByUtilisateurAndEtatOrderByDateDebutDesc(Utilisateur utilisateur, String etat) ;
}
