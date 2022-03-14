package com.rakesh.securityjwt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.securityjwt.dao.RoleRepository;
import com.rakesh.securityjwt.dto.UserRoleDTO;
import com.rakesh.securityjwt.exception.UserRoleException;
import com.rakesh.securityjwt.pojo.UserRole;


@Service

public class RoleServiceImplementation implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Override
	public UserRoleDTO createRole(UserRoleDTO userRoleDTO) throws UserRoleException {
		
		UserRole userRole = new UserRole();
		BeanUtils.copyProperties(userRoleDTO, userRole); // DTO-->POJO
		
		UserRole userRoleResponse = roleRepository.save(userRole);
		BeanUtils.copyProperties(userRoleResponse, userRoleDTO);// POJO-->DTO
		
		if(userRoleDTO==null) {
			throw new UserRoleException(true, "Unable to create the role!! try again", null);
		}
		return userRoleDTO;
	
	}

	@Override
	public UserRoleDTO getUserRoleById(Long roleId) throws UserRoleException{
		
		UserRole userRole=roleRepository.findById(roleId).get();
		UserRoleDTO userRoleDTO=new UserRoleDTO();
		BeanUtils.copyProperties(userRole, userRoleDTO);
		
		if(userRole==null) {
			throw new UserRoleException(true, "Unable to create the role!! try again", null);
		}
				return userRoleDTO;
	}

	@Override
	public List<UserRoleDTO> getAllRoles() throws UserRoleException {
		List<UserRole> listOfUserRole=roleRepository.findAll();
		List<UserRoleDTO> listOfUserRoleDTO=new ArrayList<>();
		UserRoleDTO userRoleDTO=null;
		for(UserRole role:listOfUserRole) {
			userRoleDTO =new UserRoleDTO();
			BeanUtils.copyProperties(role, userRoleDTO);
			listOfUserRoleDTO.add(userRoleDTO);
		}
		if(listOfUserRoleDTO.isEmpty()) {
			throw new UserRoleException(true, "Unable to fetch list of roles", null);
		}
			
		return listOfUserRoleDTO;
	}

	@Override
	public void deleteUserRoleById(Long roleId) throws UserRoleException,IllegalArgumentException,IllegalStateException {
		
		if(roleRepository.existsById(roleId)) {
			roleRepository.deleteById(roleId);
		}
		else {
			throw new UserRoleException(true, "Unable to delete user by id", null);
		}
		
	}
}














