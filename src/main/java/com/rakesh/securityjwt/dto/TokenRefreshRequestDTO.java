package com.rakesh.securityjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequestDTO {

	private String refreshToken;
}
