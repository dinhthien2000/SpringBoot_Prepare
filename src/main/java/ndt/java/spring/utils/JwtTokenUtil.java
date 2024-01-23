package ndt.java.spring.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ndt.java.spring.enties.User;

@Component
public class JwtTokenUtil {

	private final static long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // The token should expire after 24 hours

	@Value("${app.jwt.secret}") // specify in the application.properties file
	private String SECRET_KEY;

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

	private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	private Key key() {
		// return Keys.hmacShaKeyFor(this.SECRET_KEY.getBytes());
		return Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
	}

	// Jwt need Key object to set signWith method (only authen)
//	public String generateAccessToken(User user) {
//		Map<String, Object> claims = new HashMap<>();
//		return Jwts.builder().setClaims(claims).setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
//				.setIssuer("NdtJava").setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
//				.signWith(key(), SignatureAlgorithm.HS512).compact();
//	}

	// JWT authen and author
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
				.setIssuer("NdtJava")
				.claim("roles", user.getRoles().toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(key(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	/*
	 * validateAccessToken(): used to verify a given JWT. It returns true if the JWT is verified, or false otherwise.
	 * getSubject(): gets the value of the subject field of a given token. The subject contains User ID and email, which will be used to recreate a User object
	 * 
	 * */
	
	public boolean validateAccessToken(String token) {
		try {
			// validate jwt with secret key and build and confident (tin tưởng)jwt content
			// compact has been cryptographically signed.
			Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			LOGGER.error("JWT expired", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
		} catch (MalformedJwtException ex) {
			LOGGER.error("JWT is invalid", ex);
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("JWT is not supported", ex);
		} catch (SignatureException ex) {
			LOGGER.error("Signature validation failed");
		}
		return false;
	}
	
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	
	public Claims parseClaims(String token) {
		// Jwt set singinKey để chuyển đổi từ mã token sang dạng token  json (header, payload, signing), sau khi đã chuyển sang json ta có thể get header, body (payload), singing
		// string token -> json jwt -> getHeader, getBody (payload), getSigningKey
		return Jwts.parserBuilder()
		    .setSigningKey(key())
		    .build()
		    .parseClaimsJws(token)
		    .getBody();
	}

}
