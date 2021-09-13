package cm.deepdream.pointage.server.dao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cm.deepdream.pointage.model.Visiteur;
@Repository
public interface VisiteurDAO extends CrudRepository<Visiteur, Long>{
	public List<Visiteur> findByDateDernVisiteBetween (LocalDate date1, LocalDate date2) ;
	public List<Visiteur> findByDateDernVisiteAfter (LocalDate date) ;
	public List<Visiteur> findByOrderByNbVisitesDesc ( ) ;
	public List<Visiteur> findByNbVisitesBetween (Integer nbVisites1, Integer nbVisites2) ;
	@Query(value = "select count(*)>0 from Visiteur v where v.typeId=:typeId and v.numeroId=:numeroId")
	public boolean existsByTypeIdAndNumeroId (@Param("typeId") String typeId, @Param("numeroId") String numeroId) ;
	public Visiteur findByTypeIdAndNumeroId (String typeId, String numeroId) ;
}
