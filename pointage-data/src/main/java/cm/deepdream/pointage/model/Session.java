package cm.deepdream.pointage.model;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Session  extends EntiteGenerique{
	@ManyToOne
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column (name = "date_debut")
	private LocalDateTime dateDebut ;
	
	@Column (name = "adresse_ip")
	private String adresseIP ;
	
	@Column (name = "localisation")
	private String localisation ;
	
	@Column (name = "equipement")
	private String equipement ;
	
	@Column (name = "date_fin")
	private LocalDateTime dateFin ;

	@Column (name = "etat")
	private String etat ;
}
