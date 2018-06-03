package com.madfooat.web.provider;

import javax.ws.rs.ext.Provider;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;

@Provider
public class CorsResponseFilter extends CrossOriginResourceSharingFilter{


}
