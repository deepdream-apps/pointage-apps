package cm.deepdream.pointage.model;
import java.time.LocalDate;
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
public class Evenement extends EntiteGenerique{
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "lieu")
	private String lieu ;
	
	@Column (name = "type")
	private String type ;
	
	@ManyToOne
	@JoinColumn (name = "id_pointfocal")
	private Employe pointFocal ;
	
	@Column(name = "date_evnmt")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateEvnmt ;
	
	@Column(name = "heure_debut")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureDebut ;
	
	@Column(name = "heure_fin")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureFin ;
	
	@Column (name = "description")
	private String description ;
}
