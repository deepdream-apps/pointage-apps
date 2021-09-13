package cm.deepdream.pointage.enums;

public enum MotifVisite {
	REUNION (1, "Evènement"), VISITE_PERSO (2, "Visite personnelle"), PRESTATION (3, "Prestation de services");
	
	int id ;
	String libelle ;
	
	MotifVisite(int id, String libelle) {
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
		if(REUNION.getId() == id) return REUNION.getLibelle() ;
		else if(VISITE_PERSO.getId() == id) return VISITE_PERSO.getLibelle() ;
		else if(PRESTATION.getId() == id) return PRESTATION.getLibelle() ;
		return null ;
	}
}
