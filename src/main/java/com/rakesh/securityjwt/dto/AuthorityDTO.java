package com.rakesh.securityjwt.dto;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO implements GrantedAuthority {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "Name of the authority",name="authority",required=true,value="authority name")
	private String authority;
}
