package com.madfooat.web.rest;

import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.madfooat.model.User;
import com.madfooat.services.UserService;

@CrossOriginResourceSharing(allowAllOrigins = true)
public class BaseWebService {

	@Autowired
	protected UserService userService;

	protected String getLoggedInUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	protected User getLoggedInUser() {
		 return userService.getUserByUserName(getLoggedInUserName());
	}
	
	protected <T> Response buildSuccessResponse(T result){
		return Response.ok(result).build();
	}

}
