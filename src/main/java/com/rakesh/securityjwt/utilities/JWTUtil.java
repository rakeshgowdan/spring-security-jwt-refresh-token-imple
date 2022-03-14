package com.rakesh.securityjwt.utilities;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//Method to generate JWT token
//Method to validate JWT token
//Method to check expire of  JWT token
@Component
@Slf4j
public class JWTUtil {

	@Value("${jwt.secret}")
	 private String SECRET_KEY ;
	
	@Value("${jwt.token-expirationInMs}")
	private Integer tokenExpirationInMs;
	
	@Value("${jwt.refreshExpirationDateInMs}")
	private Integer refreshExpirationDateInMs;

	//retrieve username from jwt token
	    public String extractUname(String token) {
	    	
	        return extractClaim(token, Claims::getSubject);
	    }
	  //retrieve expiration date from jwt token
	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	  //for retrieving any information from token we will need the secret key
	    private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	    }
	  //check if the token has expired
	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	  //generate token for user
	    public String generateToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	       
	        return createToken(claims, userDetails.getUsername());
	    }
	    //while creating the token -
		//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
		//2. Sign the JWT using the HS256 algorithm and secret key.
		//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
		// compaction of the JWT to a URL-safe string 
	    private String createToken(Map<String, Object> claims, String subject) {
	        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationInMs))
	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	    }
	    //validate token
	    public Boolean validateToken(String token, UserDetails userDetails) {
	    	
	    	 try {
	        final String username = extractUname(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    	 }
	    	 catch (SignatureException e) {
	    	      log.error("Invalid JWT signature: {}", e.getMessage());
	    	    } catch (MalformedJwtException e) {
	    	      log.error("Invalid JWT token: {}", e.getMessage());
	    	    } catch (ExpiredJwtException e) {
	    	      log.error("JWT token is expired: {}", e.getMessage());
	    	    } catch (UnsupportedJwtException e) {
	    	      log.error("JWT token is unsupported: {}", e.getMessage());
	    	    } catch (IllegalArgumentException e) {
	    	      log.error("JWT claims string is empty: {}", e.getMessage());
	    	    }
	    	 return false;
	    }
	    
	    //refresh token
	    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

			return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

		}
}
