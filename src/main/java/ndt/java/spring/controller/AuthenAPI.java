package ndt.java.spring.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import ndt.java.spring.dto.AuthenRequest;
import ndt.java.spring.dto.AuthenResponse;
import ndt.java.spring.enties.User;
import ndt.java.spring.utils.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class AuthenAPI {

	final AuthenticationManager authenticationManager;
	final JwtTokenUtil jwtUtil;

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
			Authentication authencation = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			User user = (User) authencation.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			AuthenResponse response = new AuthenResponse(user.getEmail(), accessToken);

			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
