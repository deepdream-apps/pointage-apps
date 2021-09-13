package cm.deepdream.pointage.model;
import java.time.LocalDate;
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
public class Absence extends EntiteGenerique{
	@ManyToOne
	@JoinColumn(name = "id_employe")
	private Employe employe ;
	
	@Column(name = "date_absence")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateAbsence ;
	
	@Column (name = "jour_semaine")
	private String jourSemaine ;
	
	@Column (name = "justifiee")
	private int justifiee ;
	
	@Column (name = "libelle_justifiee")
	private String libelleJustifiee ;
	
	@Column (name = "justification")
	private String justification ;
	
}
