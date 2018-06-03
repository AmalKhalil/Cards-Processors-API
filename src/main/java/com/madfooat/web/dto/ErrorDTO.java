package com.madfooat.web.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO implements Serializable{

	private static final long serialVersionUID = -6526257034358793920L;
	private String errorCode;
	private String errorMessage;

	public ErrorDTO(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
