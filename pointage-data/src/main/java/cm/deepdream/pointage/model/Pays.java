package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Pays extends EntiteGenerique{
	@Column (name = "code")
	private String code ;
	
	@Column (name = "libelle")
	private String libelle ;
}
