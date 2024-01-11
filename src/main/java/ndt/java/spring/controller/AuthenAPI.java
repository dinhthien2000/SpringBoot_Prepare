package ndt.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import ndt.java.spring.dto.AuthenRequest;
import ndt.java.spring.dto.AuthenResponse;
import ndt.java.spring.enties.User;
import ndt.java.spring.repository.UserRepository;
import ndt.java.spring.utils.ColorSysoutUtil;
import ndt.java.spring.utils.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class AuthenAPI {

	final private AuthenticationManager authenticationManager;
	final private JwtTokenUtil jwtUtil;
	final private UserRepository repository;


	/*
	 * Ở đây, URI là /auth/login và chúng tôi sử dụng trình quản lý xác thực để xác
	 * thực người dùng. Đó là lý do tại sao chúng ta cần hiển thị một loại Bean
	 * AuthenticationManager trong lớp cấu hình bảo mật. Trong trường hợp thông tin
	 * xác thực không hợp lệ, BradCredentialsException sẽ được ném ra và API trả về
	 * trạng thái HTTP 401 (Không được phép). Nếu hợp lệ, nó sử dụng lớp
	 * JwtTokenUtil để tạo mã thông báo truy cập mới, sau đó được gắn vào đối tượng
	 * phản hồi thuộc loại AuthResponse.
	 * 
	 */

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenRequest request) {
		try {
			// Create Authentication object ( gồm Object principal (username), Object credentials (passwrord)) 
			// from request  with username and password parameters
			Authentication authencation = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			User user = (User) authencation.getPrincipal(); // authentication request with username and password, getPrincipal() có thể lấy được thông tin user được truyền vào
			System.out.println(ColorSysoutUtil.GREEN_BOLD + user.toString() + ColorSysoutUtil.RESET);
			String accessToken = jwtUtil.generateAccessToken(user);
			System.out.println(accessToken);
			AuthenResponse response = new AuthenResponse(user.getEmail(), accessToken);

			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	
	@GetMapping("/auth/signup/default")
	public ResponseEntity<?> signUpDefault() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode("Abc@1234");
		
		User user = new User("ndtjava@gmail.com",password);
		User newUser = repository.save(user);
		
		return ResponseEntity.ok(newUser);
	}

	@GetMapping("/auth/signup")
	public ResponseEntity<?> signUp(String username, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// String password = encoder.encode("Abc@1234");
		
		User user = new User(username,encoder.encode(password));
		User newUser = repository.save(user);
		
		return ResponseEntity.ok(newUser);
	}
	
	@PostMapping("/auth/signup")
	public ResponseEntity<?> signUp(@RequestBody @Valid User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// String password = encoder.encode("Abc@1234");
		
		// User user = new User(username,encoder.encode(password));
		user.setPassword(encoder.encode(user.getPassword()));
		User newUser = repository.save(user);
		AuthenResponse response = new AuthenResponse(newUser.getEmail(), "Singup success !!");
		return ResponseEntity.ok(response);
	}
}
