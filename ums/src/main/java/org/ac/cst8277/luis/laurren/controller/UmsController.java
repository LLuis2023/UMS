package org.ac.cst8277.luis.laurren.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ac.cst8277.luis.laurren.model.security.RoleEnum;
import org.ac.cst8277.luis.laurren.request.UserRequest;
import org.ac.cst8277.luis.laurren.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UmsController {

	@Autowired
	private UserService userService;

	Logger logger = LoggerFactory.getLogger(UmsController.class);

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/user/create")
	public Mono<ResponseEntity<Map<String, Object>>> userCreate(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody UserRequest userRequest) {
		Map<String, Object> response = new HashMap<>();
		try {

			List<RoleEnum> roleEnums = new ArrayList<>();
			roleEnums.add(RoleEnum.ROLE_ADMIN);
			if (userService.validateAPI(authorization, roleEnums)) {
				response.put("code", 200);
				response.put("data", userService.userCreate(userRequest));
				response.put("message", "User created successfuly");
				return Mono.just(ResponseEntity.ok(response));
			} else {
				response.put("code", 401);
				response.put("message", "Unautharized");
				return Mono.just(ResponseEntity.ok(response));
			}
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/user/{userId}")
	public Mono<ResponseEntity<Map<String, Object>>> getUserById(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<RoleEnum> roleEnums = new ArrayList<>();
			roleEnums.add(RoleEnum.ROLE_ADMIN);
			if (userService.validateAPI(authorization, roleEnums)) {
				response.put("code", 200);
				response.put("data", userService.getUserById(userId));
				response.put("message", "User retreived successfuly");
				return Mono.just(ResponseEntity.ok(response));
			} else {
				response.put("code", 401);
				response.put("message", "Unautharized");
				return Mono.just(ResponseEntity.ok(response));
			}
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/users")
	public Mono<ResponseEntity<Map<String, Object>>> getAllUser(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<RoleEnum> roleEnums = new ArrayList<>();
			roleEnums.add(RoleEnum.ROLE_ADMIN);
			if (userService.validateAPI(authorization, roleEnums)) {
				response.put("code", 200);
				response.put("data", userService.getAllUsers());
				response.put("message", "All user retrieved successfuly");
				return Mono.just(ResponseEntity.ok(response));
			} else {
				response.put("code", 401);
				response.put("message", "Unautharized");
				return Mono.just(ResponseEntity.ok(response));
			}
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@GetMapping("/roles")
	public Mono<ResponseEntity<Map<String, Object>>> getAllRoles(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<RoleEnum> roleEnums = new ArrayList<>();
			roleEnums.add(RoleEnum.ROLE_ADMIN);
			if (userService.validateAPI(authorization, roleEnums)) {
				response.put("code", 200);
				response.put("data", userService.getAllRoles());
				response.put("message", "All roles retrieved successfuly");
				return Mono.just(ResponseEntity.ok(response));
			} else {
				response.put("code", 401);
				response.put("message", "Unautharized");
				return Mono.just(ResponseEntity.ok(response));
			}
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/user/{userId}")
	public Mono<ResponseEntity<Map<String, Object>>> userDelete(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String userId) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<RoleEnum> roleEnums = new ArrayList<>();
			roleEnums.add(RoleEnum.ROLE_ADMIN);
			if (userService.validateAPI(authorization, roleEnums)) {
				response.put("code", 200);
				response.put("data", "User successfully deleted");
				response.put("message", "User deleted successfuly");
				return Mono.just(ResponseEntity.ok(response));
			} else {
				response.put("code", 401);
				response.put("message", "Unautharized");
				return Mono.just(ResponseEntity.ok(response));
			}
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

}
