package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Poste extends EntiteGenerique{
	@Column (name = "abreviation")
	private String abreviation ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@ManyToOne
	@JoinColumn (name = "id_rang")
	private Rang rang ;
	
	@ManyToOne
	@JoinColumn (name = "id_unite_administrative")
	private UniteAdministrative uniteAdministrative ;
}
