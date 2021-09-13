package cm.deepdream.pointage.enums;

public enum EtatContrat {
	EN_COURS (1, "En Cours"), RESILIE (0, "Resilie"), SUSPENDU (2, "Suspendu") ;
	
	int id ;
	String libelle ;
	
	EtatContrat(int id, String libelle) {
		this.id = id ;
		this.libelle = libelle ;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public static String getLibelle(int id) {
		if(EN_COURS.getId() == id) return EN_COURS.getLibelle() ;
		else if(RESILIE.getId() == id) return RESILIE.getLibelle() ;
		else if(SUSPENDU.getId() == id) return SUSPENDU.getLibelle() ;
		return null ;
	}
}
