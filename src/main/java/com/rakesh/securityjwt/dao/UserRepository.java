package com.rakesh.securityjwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.rakesh.securityjwt.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {

	
	public User findByuname(String uname);
	
	Boolean existsBymailId(String email);
	Boolean existsByuname(String uname);
}
