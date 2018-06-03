package com.madfooat.web.dto;

import java.io.Serializable;

public class ResponseWrapper<T> implements Serializable{

	private static final long serialVersionUID = -6526257034358793920L;
	private boolean success;
	private T result;
	private String errorCode;
	private String errorMessage;

	public ResponseWrapper(String errorCode, String errorMessage) {
		super();
		this.success = false;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ResponseWrapper(T result) {
		super();
		this.success = true;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
