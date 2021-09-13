package cm.deepdream.pointage.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
@MappedSuperclass
@Data
@EqualsAndHashCode (callSuper = false)
public abstract class EntiteGenerique implements Serializable{
	@Id
	@Column(name = "id")
	private Long id ;
	
	@Column(name = "createur")
	private String createur ;
	
	@Column(name = "modificateur")
	private String modificateur ;
	
	@Column (name = "date_creation")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dateCreation ;
	
	@Column (name = "date_dern_maj")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dateDernMaj ;


}
