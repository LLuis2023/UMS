package org.ac.cst8277.luis.laurren.github;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ac.cst8277.luis.laurren.authentication.PBKDF2Encoder;
import org.ac.cst8277.luis.laurren.model.AuthUser;
import org.ac.cst8277.luis.laurren.model.GitUsers;
import org.ac.cst8277.luis.laurren.model.security.AuthRequest;
import org.ac.cst8277.luis.laurren.model.security.AuthResponse;
import org.ac.cst8277.luis.laurren.request.UserRequest;
import org.ac.cst8277.luis.laurren.services.AuthUserService;
import org.ac.cst8277.luis.laurren.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/")
public class GitHubController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@GetMapping
	public String index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
		model.addAttribute("username", oauth2User.getAttributes().get("login"));
		GitUsers gitUsers = userService.findGitUserById(oauth2User.getAttributes().get("login").toString());
		if (null != gitUsers) {
			gitUsers.setGitToken(authorizedClient.getAccessToken().getTokenValue());
			

			Date expiration = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(expiration); 
			c.add(Calendar.DATE, 1);
			expiration = c.getTime();
			
			gitUsers.setExpiration(expiration);
			gitUsers = userService.updateGitUser(gitUsers);
		}
		return "index";
	}

	@PostMapping("/git/user/create")
	public Mono<ResponseEntity<Map<String, Object>>> userCreate(@RequestBody UserRequest userRequest) {
		Map<String, Object> response = new HashMap<>();
		try {

			response.put("code", 200);
			response.put("data", userService.createGitUser(userRequest));
			response.put("message", "Git User created successfuly");
			return Mono.just(ResponseEntity.ok(response));
		} catch (Exception e) {
			response.put("code", 400);
			response.put("data", null);
		}
		return Mono.just(ResponseEntity.ok(response));
	}

	@PostMapping("/git/login")
	public Mono<ResponseEntity<AuthResponse>> gitLogin(@RequestBody AuthRequest ar) {
		Mono<AuthUser> authUser = authUserService.findByUserNameGit(ar.getUsername(), ar.getPassword());
		if (null != authUser.block()) {
			String gitToken = authUser.block().getGitToken();
			return Mono.just(ResponseEntity.ok(new AuthResponse(gitToken)));
		}

		return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

}
