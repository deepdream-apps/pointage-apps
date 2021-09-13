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
public class Prestation extends EntiteGenerique{
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "type")
	private int type ;
	
	@Column (name = "libelle_type")
	private String libelleType ;
	
	@Column(name = "date_prest")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate datePrest ;
	
	@ManyToOne
	@JoinColumn (name = "id_pointfocal1")
	private Employe pointFocal1 ;
	
	@ManyToOne
	@JoinColumn (name = "id_pointfocal2")
	private Employe pointFocal2 ;
	
	@Column(name = "heure_debut")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureDebut ;
	
	@Column(name = "heure_fin")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureFin ;
	
	@Column (name = "description")
	private String description ;
}
