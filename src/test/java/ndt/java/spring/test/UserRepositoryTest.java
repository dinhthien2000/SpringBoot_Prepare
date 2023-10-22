package ndt.java.spring.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import lombok.RequiredArgsConstructor;
import ndt.java.spring.enties.User;
import ndt.java.spring.repository.UserRepository;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@RequiredArgsConstructor
public class UserRepositoryTest {
	
	final private UserRepository repository;
	
	@Test
	public void testCreateUser() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode("Abc@1234");
		
		User user = new User("ndtjava@gmail.com",password);
		User newUser = repository.save(user);
		
		assertThat(newUser).isNotNull();
		assertThat(newUser.getId()).isGreaterThan(0);
	}
	
	
}
