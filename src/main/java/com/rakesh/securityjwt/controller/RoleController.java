package com.rakesh.securityjwt.controller;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakesh.securityjwt.dto.CommonResponse;
import com.rakesh.securityjwt.dto.UserRoleDTO;

import com.rakesh.securityjwt.service.RoleService;

import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/api/v1/userRole")

@Api(value = "RoleController Resource", description = "RoleController Resource !!!!", produces = "application/json")
public class RoleController {

	@Autowired
	private RoleService roleService;

	
	@PostMapping("/role")
	@Operation(summary = "craete role", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})
	public CommonResponse createRole(@RequestBody UserRoleDTO roleDTO) {
		return  new CommonResponse(201, false, "Role created successfully!!", new Date(), roleService.createRole(roleDTO), null);
	}

	@GetMapping("/role")
	@Operation(summary = "List of roles", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})

	public CommonResponse getAllRoles() {
		return new CommonResponse(200, false, "data fetched successfully", new Date(), roleService.getAllRoles(), null);
	}

	@DeleteMapping("/role/{roleId}")
	@Operation(summary = "delete role in the System ", responses = { 
			@ApiResponse(responseCode="200",description="Data Found",content=@Content(mediaType="application/json") ),
			@ApiResponse(responseCode="404",description="Data Not Found",content=@Content(mediaType="application/json")),
			@ApiResponse(responseCode="500",description="Internal Error",content=@Content(mediaType="application/json") )
	})
	public CommonResponse deleteRole(@PathVariable Long roleId) {
		roleService.deleteUserRoleById(roleId);
		return new CommonResponse(200, false, "Role deleted successfully!!", new Date(), null, null);
		
	}
}
