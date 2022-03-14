package com.rakesh.securityjwt.controller;


import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.securityjwt.dto.CommonResponse;

import io.swagger.annotations.Api;



@RestController
@RequestMapping("/api/v1")
@Api(value = "HomeController Resource", description = "HomeController Resource !!!!",produces = "application/json")
public class HomeController {
	
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public CommonResponse admin() {
		return new CommonResponse(200, false, "Hello!! Admin", new Date(), "Hello!! admin", null);
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public CommonResponse user() {
		return new CommonResponse(200, false, "Hello!! user", new Date(), "Hello!! admin", null);
		
	}
	@GetMapping("/manager")
	public CommonResponse manager() {
		return new CommonResponse(200, false, "Hello!! Manager", new Date(), "Hello!! admin", null);
	}
	
}
