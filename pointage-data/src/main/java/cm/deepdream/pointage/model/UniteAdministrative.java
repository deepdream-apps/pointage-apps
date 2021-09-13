package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class UniteAdministrative extends EntiteGenerique{
	@Column (name = "abreviation")
	private String abreviation ;
	
	@Column (name = "libelle")
	private String libelle ;
}
