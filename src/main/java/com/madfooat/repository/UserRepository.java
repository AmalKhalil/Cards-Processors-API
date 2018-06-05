package com.madfooat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.madfooat.enums.Role;
import com.madfooat.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUserNameAndRole(String userName, Role role);
	
	User findByUserName(String userName);
}
