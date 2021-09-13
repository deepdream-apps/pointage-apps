package cm.deepdream.pointage.server.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Horaire;
import cm.deepdream.pointage.model.Statut;
@Repository
public interface HoraireDAO extends CrudRepository<Horaire, Long>{
	public Horaire findByStatut(Statut statut) ; 
}
