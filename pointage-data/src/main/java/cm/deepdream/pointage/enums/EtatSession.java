package cm.deepdream.pointage.enums;

public enum EtatSession {
	EN_COURS ("En Cours"), TERMINE ("Termine") ;
	
	String code ;
	
	EtatSession(String code) {
		this.code = code ;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code ;
	}
	

}
