package ndt.java.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/","/hello","/index"})
public class PermitAllController {

	@GetMapping
	public String index() {
		return " Welcome to website ahihi ^^ ";
	}
}
