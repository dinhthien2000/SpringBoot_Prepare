package ndt.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ndt.java.spring.enties.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
