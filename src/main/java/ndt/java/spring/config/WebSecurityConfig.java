package ndt.java.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import ndt.java.spring.repository.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	static String[] LIST_MATCHER ={"/*"};
	
	final UserRepository repository;
	
	@Bean
	/*Because we don’t use classic web so disable CSRF and no session management needed*/
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		return http.csrf(c->c.disable())
			.authorizeHttpRequests(auth->auth.requestMatchers(LIST_MATCHER).permitAll())
			.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				return repository.findByEmail(username)
						.orElseThrow(
								()-> new UsernameNotFoundException("Username "+username+" not found")
						);
			}
		};
	}
	
}
