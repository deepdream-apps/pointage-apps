package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Rang extends EntiteGenerique{
	@Column (name = "abreviation")
	private String abreviation ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "niveau")
	private int niveau ;
}
