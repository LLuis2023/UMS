package org.ac.cst8277.luis.laurren.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.ac.cst8277.luis.laurren.model.security.RoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	@Getter
	@Setter
	private Boolean enabled;

	@Getter
	@Setter
	private List<RoleEnum> roles;

	private String gitToken;

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name()))
				.collect(Collectors.toList());
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getGitToken() {
		return gitToken;
	}

	public void setGitToken(String gitToken) {
		this.gitToken = gitToken;
	}

}