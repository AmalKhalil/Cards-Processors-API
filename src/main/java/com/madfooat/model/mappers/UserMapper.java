package com.madfooat.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.madfooat.model.User;
import com.madfooat.web.dto.UserDTO;

@Mapper
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper( UserMapper.class ); 
	 
    UserDTO userToUserDto(User user); 
}
