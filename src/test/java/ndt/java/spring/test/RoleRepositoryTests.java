package ndt.java.spring.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import ndt.java.spring.enties.Role;
import ndt.java.spring.enties.User;
import ndt.java.spring.repository.RoleRepository;
import ndt.java.spring.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	@Autowired
	private RoleRepository repoRole;
	@Autowired
	private UserRepository repoUser;

//	@Test
//	public void createRole() {
//		Role admin = new Role("ROLE_ADMIN");
//		Role editor = new Role("ROLE_EDITOR");
//		Role customer = new Role("ROLE_CUSTOMER");
//
//		repoRole.saveAll(List.of(admin, editor, customer));
//
//		long count = repoRole.count();
//		assertEquals(3, count);
//	}

	// assign roles ID 3 to the user ID 4
	@Test
	public void testAssignRoleToUser() {
		Integer userId = 3;
		Integer roleId = 3;
		User user = repoUser.findById(userId).get();

		user.addRole(new Role(roleId));
		User updatedUser = repoUser.save(user);
		assertThat(updatedUser.getRoles()).hasSize(1);
	}

	// assign 2 roles ID 1 and 2 to the user 2
//	@Test
//	public void testAssignRoleToUser2() {
//		Integer userId = 1;
//		User user = repoUser.findById(userId).get();
//		user.addRole(new Role(1));
//		user.addRole(new Role(2));
//
//		User updatedUser = repoUser.save(user);
//		assertThat(updatedUser.getRoles()).hasSize(2);
//
//	}
}
