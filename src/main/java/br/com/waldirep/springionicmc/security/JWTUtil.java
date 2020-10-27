package br.com.waldirep.springionicmc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // Anotação que serve para essa classe ser injetada como componente
public class JWTUtil {
	
	@Value("${jwt.secret}") // Pegando o valor do jwt.secret no application.properties -- Palavra secreta que sera embaralhada com o token
	private String secret;
	
	
	@Value("${jwt.expiration}") // Pegando o valor do jwt.expiration no application.properties -- Tempo de expirassão do Token
	private Long expiration;
	
	
	/**
	 * Método que gera o token
	 * 
	 * @param email
	 * @return
	 */
	public String generateToken(String username) {
		return Jwts.builder() // Metodo da biblioteca JWT que gera o token
				.setSubject(username) // recebe o usuario que veio de argumento
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) // Setando o tempo de expiração do token que pega o horario atual do sistema e soma com o definido por nós que se encontra no application.properties
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()) // Tipo de assinatura do token( Pega um array de bytes ), que recebe um tipo de algoritmo qe junta com o secret, que é a palavra secreta
				.compact(); // Junta tudo
	}

}
