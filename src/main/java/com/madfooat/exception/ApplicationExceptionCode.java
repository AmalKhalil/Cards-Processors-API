package com.madfooat.exception;

import lombok.Getter;

@Getter
public enum ApplicationExceptionCode {

	SYSTEM_ERROR("1000"), 
	ACCESS_FORBIDDEN("1001"),

	UNKNOW_MERCHANT("001"),
	EMPTY_FILE("002"),
	INVALIDE_RECORD_SIZE("003"),
	INVALIDE_TRANSACTION_CURRENCY("004"),
	INVALIDE_DATE_FORMAT("005"),
	NOT_MERCHANT("006"),
	INVALID_USERNAMR_PASSWORD("007");
	


	private String code;

	ApplicationExceptionCode(String exceptionCode) {
		this.code = exceptionCode;
	}
}
