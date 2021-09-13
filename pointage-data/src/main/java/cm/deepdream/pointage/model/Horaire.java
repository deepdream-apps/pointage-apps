package cm.deepdream.pointage.model;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Horaire extends EntiteGenerique{
	@Column(name = "heure_arrivee")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureArrivee ;
	
	@Column(name = "heure_depart")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureDepart ;
	
	@Column(name = "marge_horaire")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime margeHoraire ;
	
	@Column(name = "debut_pause")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime debutPause ;
	
	@Column(name = "fin_pause")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime finPause ;
	
	@ManyToOne
	@JoinColumn (name = "id_statut")
	private Statut statut ;
	
	@ManyToOne
	@JoinColumn (name = "id_rang")
	private Rang rang ;
	
	@ManyToOne
	@JoinColumn (name = "id_site")
	private Site site ;
}
