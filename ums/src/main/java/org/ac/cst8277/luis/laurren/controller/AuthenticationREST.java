package org.ac.cst8277.luis.laurren.controller;

import org.ac.cst8277.luis.laurren.authentication.JWTUtil;
import org.ac.cst8277.luis.laurren.authentication.PBKDF2Encoder;
import org.ac.cst8277.luis.laurren.model.security.AuthRequest;
import org.ac.cst8277.luis.laurren.model.security.AuthResponse;
import org.ac.cst8277.luis.laurren.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class AuthenticationREST {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private PBKDF2Encoder passwordEncoder;
	@Autowired
	private AuthUserService userService;

	@PostMapping("/login")
	public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
		return userService.findByUsername(ar.getUsername())
				.filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
				.map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	}

}
