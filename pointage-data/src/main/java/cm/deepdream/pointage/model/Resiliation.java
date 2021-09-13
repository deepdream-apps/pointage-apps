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
public class Resiliation extends EntiteGenerique{
	@ManyToOne
	@JoinColumn(name = "id_employe")
	private Employe employe ;
	
	@Column(name = "date_resiliation")
	@DateTimeFormat (pattern = "yyyy-MM-dd")
	private LocalDate dateResiliation ;
	
	@Column (name = "motif")
	private String motif ;
	
}
