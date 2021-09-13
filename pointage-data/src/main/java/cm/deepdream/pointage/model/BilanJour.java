package cm.deepdream.pointage.model;
import java.time.LocalDateTime;
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
public class BilanJour extends EntiteGenerique{
	@Column(name = "date_jour")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDateTime dateJour ;
	
	@Column (name = "volume_horaire")
	private String volumeHoraire ;
	
	@Column(name = "heure_arrivee")
	@DateTimeFormat (pattern = "HH:mm:ss")
	private LocalTime heureArrivee ;
	
	@Column(name = "heure_depart")
	@DateTimeFormat (pattern = "HH:mm:ss")
	private LocalTime heureDepart ;
	
	@Column (name = "id_employe")
	private long idEmploye ;
	
	@Transient
	private Employe employe ;
}
