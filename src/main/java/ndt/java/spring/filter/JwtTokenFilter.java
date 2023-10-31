package ndt.java.spring.filter;

import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ndt.java.spring.enties.User;
import ndt.java.spring.utils.ColorSysoutUtil;
import ndt.java.spring.utils.JwtTokenUtil;

@Component
@RequiredArgsConstructor

// class JwtTokenFilter này chúng ta có thể custom Filter trong chuỗi filter (chain filter) của spring filter chain, chỉ cần kế thừa OncePerRequestFilter để có thể tạo ra hoặc custom thêm 1 filter cho phù hợp mục đích của mình
public class JwtTokenFilter extends OncePerRequestFilter {

	/*
	 * 
	 * Here, this filter class extends the OncePerRequestFilter class to guarantee a single execution per request. When it comes into play, the doFilterInternal() method gets invoked. Here’s how it works:
			+ If the Authorization header of the request doesn’t contain a Bearer token, it continues the filter chain without updating authentication context.
			+ Else, if the token is not verified, continue the filter chain without updating authentication context.
			+ If the token is verified, update the authentication context with the user details ID and email. In other words, it tells Spring that the user is authenticated, and continue the downstream filters.
	 * 
	 * 
	 * 
	 * */
	final JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (!hasAuthorizationBearer(request)) {
			filterChain.doFilter(request, response); // Causes the next filter in the chain to be invoked, or if the calling filter is the last filter in the chain,causes the resource at the end of the chain to be invoked.
			return; // nếu không đạt yêu cầu header Authorization thì false ở filter này
		}
		
		String token = getAccessToken(request); // lấy token từ header request
		if (!jwtTokenUtil.validateAccessToken(token)) {
			filterChain.doFilter(request, response);
			return; // nếu token không hợp lệ thì false ở filter này
		}
		
		setAuthencationContext(token, request); // thiết lập cấu hình context để token có thể sử dụng lần sau, nếu chưa hết hạn token
		filterChain.doFilter(request, response);
	}
	
	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		boolean flag = true;
		String header = request.getHeader("Authorization");
		
		// nếu header Authorization dùng để xác thực không có ở header (null)
		// và nếu có header Authorization mà giá trị header không bắt đầu bằng Bearer thì sẽ trả về false 
		// còn ngược lại header Authorization có giá trị (null) và value có Bearer là true
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			flag = false;
		}
		return flag;
	}
	
	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		return token;
	}
	
	// context : ngữ cảnh
	private void setAuthencationContext(String token, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(token);
		
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(userDetails, null, null); // principal, credentials, authorities 
		
		// Triển khai AuthenticationDetailsSource để xây dựng đối tượng chi tiết từ đối tượng HttpServletRequest, tạo WebAuthenticationDetails
		authenticationToken.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
				);
		
		// lấy context spring secutiry hiện tại (lấy cấu hình), sau đó thực hiện việc thiết lập jwt token vào filter để xác thực token ở header của request xem có đúng không mới cho qua bộ lọc JwtTokenFilter này 
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	
	}
	
	private UserDetails getUserDetails(String token) {
		User userDetail = new User(); // tạo đối tượng user để thiết lập thuộc tính để trả về khi phân tích jwt token
		String[] subjectToken = jwtTokenUtil.getSubject(token).split(","); // vì subject của token được tạo ra với chuỗi ghép giữa id,email (VD: 1,thien@gmail.com)
		
		userDetail.setId(Integer.parseInt(subjectToken[0])); // thiết lập id từ subject của jwt token cho user object
		userDetail.setEmail(subjectToken[1]); // thiết lập email từ subject của jwt token cho user object
		
		return userDetail; 
	}
}
