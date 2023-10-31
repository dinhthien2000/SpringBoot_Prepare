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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ndt.java.spring.filter.JwtTokenFilter;
import ndt.java.spring.repository.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	static String[] LIST_MATCHER = { "/", "/auth/signup", "/auth/login", "/hello", "/index" };

	final UserRepository repository;
	final JwtTokenFilter jwtTokenFilter;

	@Bean
	/*
	 * 1.Because we don’t use classic web so disable CSRF and no session management
	 * needed
	 * 
	 * 2.Here, the exception handling code ensures that the server will return HTTP status 401 (Unauthorized) if any error occurs during authentication process. And the last line adds our custom filter before the UsernameAndPasswordAuthenticationFilter in Spring Security filters chain.
	 */
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//1.
		http.csrf(c -> c.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(LIST_MATCHER).permitAll().anyRequest().authenticated())
				.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//2.
		http.exceptionHandling(t -> t.authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}));

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				return repository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
			}
		};
	}

}
