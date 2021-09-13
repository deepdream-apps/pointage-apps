package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class JourFerie extends EntiteGenerique{
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "jour_semaine")
	private String jourSemaine ;
	
	@Column (name = "jour_mois")
	private String jourMois ;
	
	@Column (name = "mois")
	private String mois ;
	
	@Column (name = "annee")
	private String annee ;
	

}
