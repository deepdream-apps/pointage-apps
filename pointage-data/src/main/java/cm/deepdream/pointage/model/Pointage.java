package cm.deepdream.pointage.model;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Pointage extends EntiteGenerique{
	@ManyToOne
	@JoinColumn (name = "id_employe")
	private Employe employe ;
	
	@Column(name = "date_pointage")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate datePointage ;
	
	@Column (name = "jour_semaine")
	private String jourSemaine ;
	
	@Column(name = "heure_requise")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heureRequise ;
	
	@Column(name = "heure")
	@DateTimeFormat (pattern = "HH:mm")
	private LocalTime heure ;
	
	@Column(name = "direction")
	private String direction ;
	
	@ManyToOne
	@JoinColumn (name = "id_site")
	private Site site ;
	
	@Lob
	@Column (name = "photo")
    private byte[] photo ;
	
	@Lob
	@Column (name = "empreinte")
    private byte[] empreinte ;
	
	@Column (name = "type")
	private String type ;
}
