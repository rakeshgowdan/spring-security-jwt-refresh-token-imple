package com.rakesh.securityjwt.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rakesh.securityjwt.dto.CommonResponse;
import com.rakesh.securityjwt.dto.UserDTO;
import com.rakesh.securityjwt.dto.UserRequestDTO;
import com.rakesh.securityjwt.dto.UserResponseDTO;
import com.rakesh.securityjwt.pojo.RefreshToken;
import com.rakesh.securityjwt.service.RefreshTokenService;
import com.rakesh.securityjwt.service.UserServiceImple;
import com.rakesh.securityjwt.utilities.JWTUtil;


import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class UserController extends ResponseEntityExceptionHandler {

	@Autowired
	private JWTUtil jwtUtil;
	
	

	@Autowired
	private UserServiceImple userDetailServiceHandler;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/userAuthenticate")
	@Operation(summary = "authenticate user in the System ", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})
	public CommonResponse userAuthenticate(@RequestBody UserRequestDTO userRequestDTO) {
		UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userRequestDTO.getUname(), userRequestDTO.getPassword());
		
		// authenticate user
		//Authtoken & it will call the loadbyuserName() 
		//DB & authtoken ==
		try {
		authenticationManager.authenticate(authToken);
		}
		catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}
		// Generate token
		UserDetails details = userDetailServiceHandler.loadUserByUsername(userRequestDTO.getUname());
		UserDTO userDTO=(UserDTO) details;
		String token = jwtUtil.generateToken(details);
		//refresh token
		RefreshToken refreshtoken=refreshTokenService.createRefreshToken(userDTO.getUserId());
		UserResponseDTO userResponseDTO = new UserResponseDTO(userRequestDTO.getUname(), token,refreshtoken.getToken());
		
		return new CommonResponse(200, false, "User authenticated! login success", new Date(), userResponseDTO, null);
	}

	@GetMapping("/refreshToken")
	public CommonResponse refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		log.info("refreshToken called");
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
		
		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
	
		String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		log.info(token);
		return new CommonResponse(200, false, "Token re-generated successfully(refresh token)!!", new Date(), token, null);
	}
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		log.info(expectedMap.toString());
		return expectedMap;
	}
	@PostMapping("/register")
	@Operation(summary = "register user in the System ", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})
	public CommonResponse registerUser(@RequestBody UserDTO userDTO) {
		UserDTO userdto=userDetailServiceHandler.registerUser(userDTO);
		return new CommonResponse(201, false, "User registration success", new Date(), userdto, null);
	
	}
	@GetMapping("/user/currentUser")
	//@ExceptionHandler(value= {ExpiredJwtException.class})
	@Operation(summary = "get current logged in user in the System", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})
	public CommonResponse getCurrentUser(Principal authentication) {
		UserDetails details=null;
		if(authentication!=null) {
			details=userDetailServiceHandler.loadUserByUsername(authentication.getName());
		}
		return new CommonResponse(200, false, "Current user fetched successfully", new Date(), (UserDTO) details, null);
		
	}
}