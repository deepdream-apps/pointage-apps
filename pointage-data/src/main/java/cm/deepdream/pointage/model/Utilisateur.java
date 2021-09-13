package cm.deepdream.pointage.model;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Utilisateur extends EntiteGenerique{
	@Column (name = "login")
	private String login ;
	
	@Column (name = "mot_de_passe")
	private String motDePasse ;
	
	@ManyToOne
	@JoinColumn (name = "id_employe")
	private Employe employe ;
	
	@Column(name = "date_expiration")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExpiration ;
	
	@Column(name = "date_expiration_mdp")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExpirationMdp ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
	/***
	 * 0 : Non activé
	 * 1 : Activé
	 * 2 : Expiré
	 * 3 : Désactivé
	 */
	@Column (name = "etat")
	private int etat ;
	
	@Column (name = "libelle_etat")
	private String libelleEtat ;
	
	@ManyToOne
	@JoinColumn (name = "id_profil")
	private Profil profil ;
	
	@Column(name = "date_dern_conn")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateDernConn ;
	
	@Transient
	private String mdp1 ;
	
	@Transient
	private String mdp2 ;
	
	@Transient
	private String mdp3 ;
}
