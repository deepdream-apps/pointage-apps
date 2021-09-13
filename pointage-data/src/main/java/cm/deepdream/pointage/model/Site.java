package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Site extends EntiteGenerique{
	@Column (name = "code")
	private String code ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "localisation")
	private String localisation ;
}
