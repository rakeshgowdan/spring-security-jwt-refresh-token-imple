package com.rakesh.securityjwt.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_details")
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	@Column(name = "USER_NAME", nullable = false)
	private String uname;
	@Column(nullable = false)
	private String password;
	private String firstName;
	private String lastName;
	@Column(nullable = false)
	private String mailId;
	@Column(nullable = false)
	private String mobileNo;

	@JsonManagedReference //helps avoid manage circular dependency in bi-directional mapping
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", 
	joinColumns = { @JoinColumn(name = "user_id") }, 
	inverseJoinColumns = {@JoinColumn(name="role_id")})
	private Set<UserRole> roles = new HashSet<>();

}
