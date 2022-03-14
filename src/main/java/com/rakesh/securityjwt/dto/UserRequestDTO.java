package com.rakesh.securityjwt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserRequestDTO {

	@ApiModelProperty(notes = "uname ",name="name",required=true)
	private String uname;
	@ApiModelProperty(notes = "password ",name="password",required=true)
	private String password;
}
