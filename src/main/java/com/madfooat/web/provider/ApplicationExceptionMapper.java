package com.madfooat.web.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madfooat.exception.ApplicationException;
import com.madfooat.web.dto.ErrorDTO;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

	public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionMapper.class);

	@Override
	public Response toResponse(ApplicationException exception) {
		LOGGER.error("ApplicationException", exception);
		return Response.ok().entity(new ErrorDTO(exception.getExceptionCode().getCode(), exception.getMessage()))
				.build();
	}

}
