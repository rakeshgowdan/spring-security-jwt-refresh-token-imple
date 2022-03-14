package com.rakesh.securityjwt.dto;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	
	private String userName;
	private String token;
	private String refreshToken;
}
