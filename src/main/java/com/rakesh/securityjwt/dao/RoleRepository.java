package com.rakesh.securityjwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rakesh.securityjwt.pojo.UserRole;

public interface RoleRepository  extends JpaRepository<UserRole, Long>{

}
