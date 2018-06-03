package com.madfooat.web.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.web.dto.ErrorDTO;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

	public static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

	@Override
	public Response toResponse(RuntimeException exception) {
		LOGGER.error("RuntimeException", exception);
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorDTO(ApplicationExceptionCode.SYSTEM_ERROR.getCode(),
						"System error: " + exception.getMessage()))
				.build();
	}

}
