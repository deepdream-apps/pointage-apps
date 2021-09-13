package cm.deepdream.pointage.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Entreprise implements Serializable{
	@Id
	@Column (name = "id")
	private long id ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "ville")
	private String ville ;

}
