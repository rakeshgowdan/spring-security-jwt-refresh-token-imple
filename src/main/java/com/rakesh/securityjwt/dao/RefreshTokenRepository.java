package com.rakesh.securityjwt.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.rakesh.securityjwt.pojo.RefreshToken;
import com.rakesh.securityjwt.pojo.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>  {

	Optional<RefreshToken> findByToken(String token);

	  @Modifying
	  int deleteByUser(User user);
	
}
