package br.com.waldirep.springionicmc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.waldirep.springionicmc.security.JWTAuthenticationFilter;
import br.com.waldirep.springionicmc.security.JWTAuthorizationFilter;
import br.com.waldirep.springionicmc.security.JWTUtil;

/**
 * Classe que define as configurações de segurança
 * libera e bloqueia por padrão os endpoints
 * 
 * OBS : so ao colocar a dependencia no pom.xml ela ja bloqueia os endpoints
 * @author Waldir
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired // Objeto de configuração para acesso ao H2
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	// Caminhos que por padrão estão liberados, acesso ao BD H2
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
			};
	
	// Caminhos que por padrão estão liberados apenas para recuperação de dados
		private static final String[] PUBLIC_MATCHERS_GET = {
				"/produtos/**", 
				"/categorias/**",
				"/clientes/**"
				};
	
	/**
	 * Método que configura as autorizações recebe um HttpSecurity
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 * Lógica de negócio para acesso ao BD h2 que pega o profile ativo(test) em application.properties
		 */
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable(); // como o sistema e stateless e não armazena a autenticação em sessão o csrf foi desabilitado
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() //Permissão apenas para o mrthod GET para os usuarios desta lista
		.antMatchers(PUBLIC_MATCHERS).permitAll() 
		.anyRequest().authenticated(); // Libera o acesso as URLs do vetor e para todo resto exige autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil)); // registrando o filtro de autenticação
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService)); // registrando o filtro de autorização
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Garante que não sera criada sessão de usuario
	}
	
	
	/**
	 * Método do spring security de autenticação que mostra quem é o userDetailsService que esta sendo usado 
	 * e e qual e o algoritmo de codificação da senha, que no caso e o bCryptPasswordEncoder(), metodo que esta
	 * logo abaixo, que ja possuí o @Bean e por isso não existe a necessidade de @Bean tb nesse método
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	/**
	 * Método que configura os Cors
	 *  A anotação com @Bean torna disponivel esse método como componente do sistema
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
    
	/**
	 * Método que retorna a senha criptografada
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
