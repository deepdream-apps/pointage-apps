package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Categorie extends EntiteGenerique{
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "description")
	private String description ;
}
