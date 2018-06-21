package com.excilys.cdb.webservices;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.service.IUserService;
import com.excilys.cdb.webservices.security.JwtAuthenticationRequest;
import com.excilys.cdb.webservices.security.JwtAuthenticationResponse;
import com.excilys.cdb.webservices.security.JwtTokenUtil;
import com.excilys.cdb.webservices.security.JwtUser;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateWebService {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private IUserService serviceUser;

	@PostMapping(value = "${jwt.route.authentication.path}")
	public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails);
			return ResponseEntity.ok(new JwtAuthenticationResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().body(null);
		}
		
		
	}

	@GetMapping(value = "${jwt.route.authentication.refresh}")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody JwtAuthenticationRequest auth) {
		Logger logger = LoggerFactory.getLogger(AuthenticateWebService.class);
		logger.info("signup");
		serviceUser.inscription(auth.getUsername(), new BCryptPasswordEncoder().encode(auth.getPassword()));
		return createAuthenticationToken(auth);
		
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 */
	private void authenticate(String username, String password) throws AuthenticationException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

}
