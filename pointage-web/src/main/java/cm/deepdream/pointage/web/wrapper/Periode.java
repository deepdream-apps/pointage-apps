package cm.deepdream.pointage.web.wrapper;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode (callSuper = false)
public class Periode implements Serializable{
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;
}
