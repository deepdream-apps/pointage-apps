package cm.deepdream.pointage.model;
import java.time.LocalDate;
import java.time.LocalTime;
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
public class Visite extends EntiteGenerique{
	@Column (name = "type_id")
	private String typeId ;
	
	@Column (name = "numero_id")
	private String numeroId ;
	
	@Column (name = "date_pi")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate datePi ;
	
	@NotNull
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "qualite")
	private String qualite ;
	
	@Column (name = "date_visite")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateVisite ;
	
	@Column (name = "jour_semaine")
	private String jourSemaine ;
	
	@Column (name = "heure_arrivee")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureArrivee ;
	
	@Column (name = "heure_depart_prevu")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureDepartPrevu ;
	
	@Column (name = "heure_depart")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureDepart ;
	
	/**
	 * Convocation
	 * Réunion/séance de travail
	 * Audience
	 * Visite personnelle
	 **/
	@NotNull
	@Column (name = "motif")
	private Integer motif ;
	
	@Column (name = "libelle_motif")
	private String libelleMotif ;
	
	@ManyToOne 
	@JoinColumn (name = "id_pays")
	private Pays pays ;
	
	@ManyToOne 
	@JoinColumn (name = "id_employe")
	private Employe employe ;
	
	@ManyToOne 
	@JoinColumn (name = "id_evenement")
	private Evenement evenement ;
	
	@ManyToOne
	@JoinColumn (name = "id_prestation")
	private Prestation prestation ;
	
	@NotNull
	@Column (name = "numero_whatsapp")
	private String numeroWhatsapp ;
	
	@NotNull
	@Column (name = "email")
	private String email ;
	
	@Lob
	@Column (name = "photo")
    private byte[] photo ;
	
	@Column (name = "commentaires")
	private String commentaires ;
}
//https://vaadin.com/blog/saving-and-displaying-images-using-jpa