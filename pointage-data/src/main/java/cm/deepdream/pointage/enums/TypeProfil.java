package cm.deepdream.pointage.enums;

public enum TypeProfil {
	Administrateur (1), Employe (2) ;
	
	int id ;
	
	TypeProfil(int id){
		this.id = id ;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static String getLibelle(int id) {
		if(id == Administrateur.getId()) return Administrateur.toString() ;
		else if(id == Employe.getId()) return Employe.toString() ;
		return null ;
	}
}
