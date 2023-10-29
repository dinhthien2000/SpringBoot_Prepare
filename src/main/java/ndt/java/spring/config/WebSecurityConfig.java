package ndt.java.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	static String[] LIST_MATCHER ={"/*"};
	
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
	
	
}
