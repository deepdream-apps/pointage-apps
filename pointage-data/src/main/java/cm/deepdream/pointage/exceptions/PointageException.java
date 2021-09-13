package cm.deepdream.pointage.exceptions;

public class PointageException extends Exception{
	
	public PointageException() {
		super() ;
	}
	
	public PointageException(String message) {
		super(message) ;
	}
	
	public PointageException(String message, Throwable t) {
		super(message, t) ;
	}

}
