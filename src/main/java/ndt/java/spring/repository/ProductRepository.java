package ndt.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ndt.java.spring.enties.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
