package org.ac.cst8277.luis.laurren.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ac.cst8277.luis.laurren.model.AuthUser;
import org.ac.cst8277.luis.laurren.model.GitUsers;
import org.ac.cst8277.luis.laurren.model.security.RoleEnum;
import org.ac.cst8277.luis.laurren.repository.GitUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class AuthUserService {

	private Map<String, AuthUser> data;

	@Autowired
	private GitUserRepository gitUserRepository;

	@PostConstruct
	public void init() {
		data = new HashMap<>();

		// username:passwowrd -> user:user
		data.put("user", new AuthUser("user", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true,
				Arrays.asList(RoleEnum.ROLE_USER), null));

		// username:passwowrd -> admin:admin
		data.put("admin", new AuthUser("admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true,
				Arrays.asList(RoleEnum.ROLE_ADMIN), null));
	}

	public Mono<AuthUser> findByUsername(String username) {
		return Mono.justOrEmpty(data.get(username));
	}

	public Mono<AuthUser> findByUserNameGit(String userName, String password) {
		GitUsers user = gitUserRepository.findByUserNameAndPassword(userName, password);
		AuthUser authUser = null;
		if (null != user) {
			authUser = new AuthUser(user.getUserName(), user.getPassword(), true, Arrays.asList(RoleEnum.ROLE_ADMIN),
					user.getAccessToken());
		}
		return Mono.justOrEmpty(authUser);
	}

}
