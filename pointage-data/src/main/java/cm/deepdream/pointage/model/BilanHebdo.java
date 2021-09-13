package cm.deepdream.pointage.model;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class BilanHebdo extends EntiteGenerique{
	@Column(name = "date_debut")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDateTime dateDebut ;
	
	@Column(name = "date_fin")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDateTime dateFin ;
	
	@Column (name = "volume_requis")
	private int volumeRequis ;
	
	@Column (name = "volume_horaire")
	private int volumeHoraire ;
	
	@Column (name = "jours_ouvres")
	private int joursOuvres ;
	
	@Column (name = "absences")
	private int absences ;
	
	@Column (name = "id_employe")
	private long idEmploye ;
	
	@Transient
	private Employe employe ;
}
