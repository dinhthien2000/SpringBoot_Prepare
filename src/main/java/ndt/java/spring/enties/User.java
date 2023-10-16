package ndt.java.spring.enties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class User {
	@Id
	String username;
	
	String password;
	String email;
	String role;
	
	
	
}
