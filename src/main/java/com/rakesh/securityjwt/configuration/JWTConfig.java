package com.rakesh.securityjwt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rakesh.securityjwt.service.UserServiceImple;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)//this helps to add method level auth security
public class JWTConfig  extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserServiceImple userDetailServiceHandler;
	
	@Autowired
	private JWTAuthFilter jwtAuthFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthEntryPoint;
	
	//using this method, we specify what is the auth imple type
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(userDetailServiceHandler).passwordEncoder(passwordEncode());
	}

	
	//using this method, we control which end points are permitted & not permitted 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
				.csrf() 			
				//Cross-Site Request Forgery (CSRF) is an attack that forces authenticated users to submit a request to a Web application against which they are currently authenticated.
				.disable()
				.cors() 	 
				//Cross-origin resource sharing is a mechanism that allows restricted resources on a web page to be requested from another domain outside the domain from which the first resource was served
				.disable()
				.authorizeRequests()
				.antMatchers("/swagger-resources/**","/v3/**","/v2/**","/webjars/**","/swagger-ui.html","/swagger-ui/**","/api/auth/userAuthenticate","/api/v1/userRole/**","/api/auth/register")  //only allow this endpoint with out authentication
				.permitAll()
				.anyRequest().authenticated()   //for any other request authentication is mandate 
				.and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // every request should be independent & server need not to manage session
		// Add a filter to validate the tokens with every reques
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authManager() throws Exception {
		return super.authenticationManagerBean();
	}
}
