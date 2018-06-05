package com.madfooat.web.rest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madfooat.services.BatchService;

@Path("/batch")
@Service
@CrossOriginResourceSharing(allowAllOrigins = true)
public class BatchWebService extends BaseWebService {

	@Autowired
	private BatchService batchService;

	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@Multipart(value = "file") @NotNull Attachment attachment)
			throws UnsupportedEncodingException {

		batchService.uploadBatchFile(this.getLoggedInUserName(), attachment.getContentDisposition().getFilename(),
				attachment.getObject(InputStream.class));
		return buildSuccessResponse(Void.class);

	}

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getBatchs() throws UnsupportedEncodingException {
			return  buildSuccessResponse(batchService.getBatches(this.getLoggedInUser()));

		
	}

}
