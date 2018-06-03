package com.madfooat.web.dto;

import com.madfooat.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	private Long id;

	private String userName;

	private String password;

	private Role role;

}