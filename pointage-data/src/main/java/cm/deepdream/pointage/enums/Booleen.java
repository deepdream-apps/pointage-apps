package cm.deepdream.pointage.enums;

public enum Booleen {
	OUI (1, "Oui"), NON (0, "Non") ;
	
	int id ;
	String libelle ;
	
	Booleen(int id, String libelle) {
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
		if(OUI.getId() == id) return OUI.getLibelle() ;
		else if(NON.getId() == id) return NON.getLibelle() ;
		return null ;
	}
}
