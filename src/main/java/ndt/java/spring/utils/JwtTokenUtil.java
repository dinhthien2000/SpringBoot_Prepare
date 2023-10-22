package ndt.java.spring.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import ndt.java.spring.enties.User;

@Component
public class JwtTokenUtil {
	
	private final static long EXPIRE_DURATION = 24*60*60*1000; // The token should expire after 24 hours
	
	@Value("${app.jwt.secrect}") // specify in the application.properties file
	private String SECRET_KEY;
	
	 private Key key() {
		    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
		  }
	
	public String generateAccessToken(User user) {
		return Jwts.builder()
	               .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
	               .setIssuedAt(new Date())
	               .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
	               .signWith(key(),SignatureAlgorithm.HS512)
	               .compact();
	}
	
}
