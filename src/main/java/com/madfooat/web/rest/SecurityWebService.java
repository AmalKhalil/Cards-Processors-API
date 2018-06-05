package com.madfooat.web.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.springframework.stereotype.Service;

import com.madfooat.web.dto.UserDTO;

@Path("/security")
@Service
@CrossOriginResourceSharing(allowAllOrigins = true)
public class SecurityWebService extends BaseWebService{

	@Path("/login")
	@POST
	public Response login(UserDTO user) {
		 return  buildSuccessResponse(this.userService.login(user));
	  }

}
