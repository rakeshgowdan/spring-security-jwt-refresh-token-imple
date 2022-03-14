package com.rakesh.securityjwt.service;

import java.util.List;

import com.rakesh.securityjwt.dto.UserRoleDTO;



public interface RoleService {

	public UserRoleDTO createRole(UserRoleDTO userRole);
	public List<UserRoleDTO> getAllRoles();
	public UserRoleDTO getUserRoleById(Long roleId);
	public void deleteUserRoleById(Long roleId);
}
