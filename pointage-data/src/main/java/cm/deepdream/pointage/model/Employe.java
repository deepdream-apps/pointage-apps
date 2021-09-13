package cm.deepdream.pointage.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Employe extends EntiteGenerique{
	@Column (name = "civilite")
	private String civilite ;
	
	@Column (name = "matricule")
	private String matricule ;
	
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "sexe")
	private String sexe ;
	
	@ManyToOne
	@JoinColumn (name = "id_unite")
	private UniteAdministrative unite ;
	
	@ManyToOne
	@JoinColumn (name = "id_poste")
	private Poste poste ;
	
	@ManyToOne
	@JoinColumn (name = "id_statut")
	private Statut statut ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "telephone_whatsapp")
	private String telephoneWhatsapp ;
	
	@Column (name = "etat")
	private int etat ;
	
	@Column (name = "langue")
	private String langue ;
	
	@Column (name = "libelle_etat")
	private String libelleEtat ;
	
	@ManyToOne
	@JoinColumn (name = "id_site")
	private Site site ;
	
	@Column (name = "texte")
	private String texte ;
	
	@Lob
	@Column (name = "photo")
    private byte[] photo ;
	
	@Lob
	@Column (name = "empreinte")
    private byte[] empreinte ;
	
}
//https://vaadin.com/blog/saving-and-displaying-images-using-jpa