package br.com.waldirep.springionicmc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
		// TODO Auto-generated constructor stub
	}

	// A mudança no nome do método de getList() para getErrors() é importante pois no JSON ela é convertida para errors
	public List<FieldMessage> getErrors() {
		return errors;
	}
	

	/**
	 * Modificado para acrescentar um erro de cada vez
	 * 
	 * @param list
	 */
	public void addError(String fieldName, String message) {
		
		errors.add(new FieldMessage(fieldName, message)); 
		
	}

	

}
