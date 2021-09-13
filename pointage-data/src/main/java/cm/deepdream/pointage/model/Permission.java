package cm.deepdream.pointage.model;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Permission extends EntiteGenerique{
	@ManyToOne
	@JoinColumn(name = "id_profil")
	private Profil profil ;
	
	@ManyToOne
	@JoinColumn(name = "id_action")
	private Action action ;
	
}
