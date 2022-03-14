package com.rakesh.securityjwt.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rakesh.securityjwt.service.UserServiceImple;
import com.rakesh.securityjwt.utilities.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private UserServiceImple userDetailServiceHandler;

	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// get the JWT token from request header & validate
		// gets called before any controller
		try {
			String bearerToken = request.getHeader("Authorization");
			String uname = null;
			String token = null;

			// JWT Token is in the form "Bearer token". Remove Bearer word and get
			// only the Token
			if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
				// extract jwt from bearertoken
				token = bearerToken.substring(7);

				// get username from token
				uname = jwtUtil.extractUname(token);
				UserDetails userDetails = userDetailServiceHandler.loadUserByUsername(uname);
				// Once we get the token validate it.
				if (uname != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					
					log.info("--------------------------------------------------------------------------------------");
					log.error("Cannot set the Security Context");
					log.info("--------------------------------------------------------------------------------------");
				}

			} else

			{
				log.info("--------------------------------------------------------------------------------------");
				log.error("Invalid bearer token format");
				log.info("--------------------------------------------------------------------------------------");
			}
		} catch (ExpiredJwtException ex) {
			log.info("inside catch block ExpiredJwtException");
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshToken")) {
				allowForRefreshToken(ex, request);
			} else
				request.setAttribute("exception", ex);
			
		} catch (BadCredentialsException ex) {
			request.setAttribute("BadCredentialsException", ex);
			

		}
		filterChain.doFilter(request, response);
	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		log.info("inside allowForRefreshToken");
		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}

}
