package cm.deepdream.pointage.model;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class EnregistrementAbsence extends EntiteGenerique{
	@Column(name = "date_enreg")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateEnreg ;
	
	@Column (name = "nb_absences")
	private int nbAbsences ;
}
