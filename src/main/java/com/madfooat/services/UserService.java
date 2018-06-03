package com.madfooat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.madfooat.exception.ApplicationException;
import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.model.User;
import com.madfooat.model.mappers.UserMapper;
import com.madfooat.repository.UserRepository;
import com.madfooat.web.dto.UserDTO;;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	

	public UserDTO login(UserDTO userDTO)  {
		UserDTO user = getByUserName(userDTO.getUserName());
		if (user == null || !encoder.matches(userDTO.getPassword(), user.getPassword())) {
			throw new ApplicationException("Invalid Username or Password",
					ApplicationExceptionCode.INVALID_USERNAMR_PASSWORD);

		}
		return user;
	}

	public User getUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
	
	public UserDTO getByUserName(String userName) {
		return UserMapper.INSTANCE.userToUserDto(getUserByUserName(userName));
	}
}
