package ndt.java.spring.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class AuthenRequest {
	
	@NotNull @Email @Length(min = 5, max = 50)
	private String email;
	
	@NotNull @Length(min = 5, max = 50)
	private String password;
	
}
