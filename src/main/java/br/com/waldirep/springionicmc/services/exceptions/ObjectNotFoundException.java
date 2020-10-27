package br.com.waldirep.springionicmc.services.exceptions;

/*
 * Classe de excessão customizada
 */
public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// Contrutor
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	
	// Contrutor que recebe uma menssagem e outra excessao, a causa de algo que aconteceu antes
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
