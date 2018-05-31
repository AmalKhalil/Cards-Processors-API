package com.madfooat.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * General exception to be treated as BAD_REQUEST.
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@NotNull
    private ApplicationExceptionCode exceptionCode;

    public ApplicationException(String message) {
        super(message);
    }


    public ApplicationException(String message, ApplicationExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ApplicationException(ApplicationExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
