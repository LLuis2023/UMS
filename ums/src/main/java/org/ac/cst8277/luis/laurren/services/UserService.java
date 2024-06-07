/**
 * 
 */
package org.ac.cst8277.luis.laurren.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ac.cst8277.luis.laurren.authentication.JWTUtil;
import org.ac.cst8277.luis.laurren.model.AuthUser;
import org.ac.cst8277.luis.laurren.model.GitUsers;
import org.ac.cst8277.luis.laurren.model.Role;
import org.ac.cst8277.luis.laurren.model.User;
import org.ac.cst8277.luis.laurren.model.UserRoles;
import org.ac.cst8277.luis.laurren.model.security.RoleEnum;
import org.ac.cst8277.luis.laurren.repository.GitUserRepository;
import org.ac.cst8277.luis.laurren.repository.RoleRepository;
import org.ac.cst8277.luis.laurren.repository.UserRepository;
import org.ac.cst8277.luis.laurren.repository.UserRoleRepository;
import org.ac.cst8277.luis.laurren.request.UserRequest;
import org.ac.cst8277.luis.laurren.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private GitUserRepository gitUserRepository;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	public User userCreate(UserRequest userRequest) {

		User user = new User();
		user.setCreated(new Date());
		UUID uuid = UUID.randomUUID();
		user.setUserId(uuid.toString());
		user.setEmail(userRequest.getEmail());
		user.setName(userRequest.getName());
		user.setPassword(userRequest.getPassword());
		userRepository.save(user);
		return user;
	}

	public GitUsers createGitUser(UserRequest userRequest) {

		GitUsers user = new GitUsers();
		user.setCreated(new Date());
		user.setUserName(userRequest.getName());
		user.setPassword(userRequest.getPassword());
		gitUserRepository.save(user);
		return user;
	}

	public GitUsers updateGitUser(GitUsers gitUsers) {

		AuthUser authUser = new AuthUser();
		authUser.setUsername(gitUsers.getUserName());
		authUser.setRoles(Arrays.asList(RoleEnum.ROLE_ADMIN));
		String jwt = jwtUtil.generateToken(authUser);

		gitUsers.setAccessToken(jwt);
		gitUserRepository.save(gitUsers);
		return gitUsers;
	}

	public GitUsers findGitUserById(String userName) {
		return gitUserRepository.findByUserName(userName);
	}

	public void userDelete(Long id) {
		userRepository.deleteById(id);
	}

	public UserResponse getUserById(String userId) {

		User user = userRepository.findByUserId(userId);
		UserResponse userResponse = new UserResponse();
		if (null != user) {
			userResponse.setUserId(user.getUserId());
			userResponse.setCreated(user.getCreated());
			userResponse.setName(user.getName());
			userResponse.setPassword(user.getPassword());
			userResponse.setEmail(user.getEmail());

			List<UserRoles> userRoles = userRoleRepository.findByUserId(user.getId());
			List<Role> rolesList = new ArrayList<>();

			for (UserRoles roles : userRoles) {
				Optional<Role> role = roleRepository.findById(roles.getRoleId());
				if (role.isPresent()) {
					rolesList.add(role.get());
				}
			}
			userResponse.setRoles(rolesList);
		}
		return userResponse;
	}

	public List<UserResponse> getAllUsers() {

		List<User> userOptional = (List<User>) userRepository.findAll();
		List<UserResponse> userResponses = new ArrayList<>();

		for (User user : userOptional) {
			UserResponse userResponse = new UserResponse();
			userResponse.setUserId(user.getUserId());
			userResponse.setCreated(user.getCreated());
			userResponse.setName(user.getName());
			userResponse.setPassword(user.getPassword());
			userResponse.setEmail(user.getEmail());

			List<UserRoles> userRoles = userRoleRepository.findByUserId(user.getId());
			List<Role> rolesList = new ArrayList<>();

			for (UserRoles roles : userRoles) {
				Optional<Role> role = roleRepository.findById(roles.getRoleId());
				if (role.isPresent()) {
					rolesList.add(role.get());
				}
			}
			userResponse.setRoles(rolesList);
			userResponses.add(userResponse);
		}

		return userResponses;
	}

	public List<Role> getAllRoles() {
		List<Role> rolesOptional = (List<Role>) roleRepository.findAll();
		return rolesOptional;
	}

	public boolean validateAPI(String token, List<RoleEnum> roleEnums) {
		String accessToken = token.split(" ")[1];
		GitUsers gitUsers = gitUserRepository.findByAccessToken(accessToken);

		if (null == gitUsers) {
			return false;
		} else if (null != gitUsers.getExpiration() && gitUsers.getExpiration().before(new Date())) {
			return false;
		} else {
			List<UserRoles> userRoles = userRoleRepository.findByUserId(gitUsers.getId());
			if (!userRoles.isEmpty()) {
				for (UserRoles userRole : userRoles) {
					Optional<Role> role = roleRepository.findById(userRole.getId());
					String roleName = role.get().getRole();
					for (RoleEnum roleEnum : roleEnums) {
						if (roleEnum.name().equalsIgnoreCase(roleName)) {
							return true;
						}
					}
				}
			}
		}
		return true;

	}
}
