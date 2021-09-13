package cm.deepdream.pointage.model;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Retard extends EntiteGenerique{
	@ManyToOne
	@JoinColumn (name = "id_employe")
	private Employe employe ;

	@Column(name = "date_retard")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateRetard ;
	
	@Column (name = "jour_semaine")
	private String jourSemaine ;
	
	@Column(name = "heure_requise")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureRequise ;
	
	@Column(name = "heure_arrivee")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureArrivee ;
	
	@Column (name = "duree_retard")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime dureeRetard ;

}
