package cm.deepdream.pointage.enums;

public enum TypePrest {
	Offert (1, "Offert"), Souscrit (2, "Souscrit") ;
	
	int id ;
	String libelle ;
	
	TypePrest(int id, String libelle){
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
		this.libelle = libelle ;
	}

	public static String getLibelle(int id) {
		if(id == Offert.getId()) return Offert.getLibelle() ;
		else if(id == Souscrit.getId()) return Souscrit.getLibelle() ;
		return null ;
	}
}
