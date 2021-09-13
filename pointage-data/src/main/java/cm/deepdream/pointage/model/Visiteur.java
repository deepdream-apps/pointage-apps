package cm.deepdream.pointage.model;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Visiteur extends EntiteGenerique{
	@Column (name = "type_id")
	private String typeId ;
	
	@Column (name = "numero_id")
	private String numeroId ;
	
	@Column (name = "date_pi")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate datePi ;
	
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "date_dern_visite")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDernVisite ;
	
	@Column (name = "nb_visites")
	private Integer nbVisites ;
	
	@ManyToOne 
	@JoinColumn (name = "id_pays")
	private Pays pays ;
	
	@Column (name = "numero_whatsapp")
	private String numeroWhatsapp ;
	
	@Column (name = "email")
	private String email ;
	
	@Lob
	@Column (name = "photo")
    private byte[] photo ;

}
//https://vaadin.com/blog/saving-and-displaying-images-using-jpa