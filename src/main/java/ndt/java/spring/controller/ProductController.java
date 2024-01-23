package ndt.java.spring.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

import ndt.java.spring.enties.Product;
import ndt.java.spring.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository repository;
	
	@PostMapping
	@RolesAllowed({"EDITOR", "ADMIN"})
	// ResponseEntity<?> với <?> is a shorthand for <? extends Object>, viết tắt
	public ResponseEntity<?> create(@RequestBody @Valid Product product) {
		repository.save(product);
		URI productUri = URI.create("/product/" + product.getId());
		// return ResponseEntity.ok(product);
		return ResponseEntity.created(productUri).body(product);
		
	}
	
	@GetMapping
	@RolesAllowed({"CUSTOMER", "EDITOR", "ADMIN"} )
	public List<Product> list() {
		return repository.findAll();
	}
}
