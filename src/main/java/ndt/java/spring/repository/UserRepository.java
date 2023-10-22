package ndt.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ndt.java.spring.enties.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
