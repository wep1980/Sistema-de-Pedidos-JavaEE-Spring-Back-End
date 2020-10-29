package br.com.waldirep.springionicmc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.waldirep.springionicmc.services.exceptions.AuthorizationException;
import br.com.waldirep.springionicmc.services.exceptions.DataIntegrityException;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;

/**
 * Manupulador de erros, claase auxiliar para interceptar as excessões (Filtro)
 * @author Waldir
 *
 */
@ControllerAdvice // Gerenciador de tratamento de excessões
public class ResourceExceptionHandler {

	
	@ExceptionHandler(ObjectNotFoundException.class) // Indicando que é um tratador de excessão do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> objectNotFound (ObjectNotFoundException e, HttpServletRequest request ){
		
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	
	@ExceptionHandler(DataIntegrityException.class) // Indicando que é um tratador de excessão do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> dataIntegrity (DataIntegrityException e, HttpServletRequest request ){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	/**
	 * Indicando que é um tratador de excessão do tipo MethodArgumentNotValidException
	 * 
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<StandardError> validation (MethodArgumentNotValidException e, HttpServletRequest request ){
		
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(),"Erro de validação", System.currentTimeMillis());
		
		/**
		 * e.getBindingResult().getFieldError() - Acessa toda a lista de campos que aparecem na exceção 
		 * MethodArgumentNotValidException
		 */
		for (FieldError fe : e.getBindingResult().getFieldErrors()) { // Percorrendo a lista de erros do framework
			err.addError(fe.getField(), fe.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	@ExceptionHandler(AuthorizationException.class) // Indicando que é um tratador de excessão do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> authorization (AuthorizationException e, HttpServletRequest request ){
		
		StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
	
}
