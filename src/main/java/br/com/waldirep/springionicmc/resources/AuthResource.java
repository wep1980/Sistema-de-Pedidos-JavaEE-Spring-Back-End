package br.com.waldirep.springionicmc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.waldirep.springionicmc.security.JWTUtil;
import br.com.waldirep.springionicmc.security.UserSS;
import br.com.waldirep.springionicmc.services.UserService;

/**
 * Classe responsavel por disponibilizar ENDPOINTS relacionados a autenticação e autorização
 * @author wepbi
 *
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	JWTUtil jwtUtil;
	

	/**
	 * Método ( ENDPOINT ) que atualiza o token 
	 * 
	 * Quando o token esta proximo de expirar a aplicação acessa esse ENDPOINT e pega um token novo
	 * 
	 * OBS : O token para acessar esse ENDPOINT precisa ainda estar valido
	 * OBS : O USUARIO precisa estar logado para acessar esse ENDPOINT
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		String token = jwtUtil.generateToken(user.getUsername()); // Gera um novo token para o usuario
		response.addHeader("Authorization", "Bearer " + token); // Adiciona o token na resposta da requisição
		return ResponseEntity.noContent().build();
	}

}
