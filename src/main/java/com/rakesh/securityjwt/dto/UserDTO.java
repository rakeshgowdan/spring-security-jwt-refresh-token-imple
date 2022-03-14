package com.rakesh.securityjwt.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements UserDetails {

	private static final long serialVersionUID = 1L;
	private long userId;
	@ApiModelProperty(notes = "Username ",name="Username",required=true)
	private String uname;
	@ApiModelProperty(notes = "password ",name="password",required=true)
	private String password;
	@ApiModelProperty(notes = "firstName ",name="firstName",required=true)
	private String firstName;
	@ApiModelProperty(notes = "lastName ",name="lastName",required=true)
	private String lastName;
	@ApiModelProperty(notes = "mailId ",name="mailId",required=true)
	private String mailId;
	@ApiModelProperty(notes = "mobileNo ",name="mobileNo",required=true)
	private String mobileNo;
	@ApiModelProperty(notes = "role id's to be assigned ",name="roles",required=true)
	private Set<UserRoleDTO> roles = new HashSet<>();


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		Set<AuthorityDTO> setOfAuthorityDTO = new HashSet<>();
		roles.forEach(role -> setOfAuthorityDTO.add(new AuthorityDTO(role.getRoleName())));
		return setOfAuthorityDTO;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	@Override
	public String getUsername() {
		return this.getUname();
	}

	

}
