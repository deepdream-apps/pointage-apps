package cm.deepdream.pointage.model;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class BilanMensuel extends EntiteGenerique{
	@Column(name = "mois")
	private String mois ;
	
	@Column(name = "annee")
	private int annee ;
	
	@Column (name = "volume_requis")
	private int volumeRequis ;
	
	@Column (name = "volume_horaire")
	private int volumeHoraire ;
	
	@Column (name = "jours_ouvres")
	private int joursOuvres ;
	
	@Column (name = "absences")
	private int absences ;
	
	@Column(name = "heure_arrivee")
	@DateTimeFormat (pattern = "HH:mm:ss")
	private LocalTime heureArrivee ;
	
	@Column(name = "heure_depart")
	@DateTimeFormat (pattern = "HH:mm:ss")
	private LocalTime heureDepart ;
	
	@Column (name = "id_employe")
	private long idEmploye ;
	
	@Column (name = "matricule_employe")
	private String matriculeEmploye ;
	
	@Column (name = "prenom_employe")
	private String nomEmploye ;
	
	@Transient
	private Employe employe ;
}
