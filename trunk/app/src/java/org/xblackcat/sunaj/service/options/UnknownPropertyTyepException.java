package org.xblackcat.sunaj.service.options;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public class UnknownPropertyTyepException extends RuntimeException {
	public UnknownPropertyTyepException() {
	}

	public UnknownPropertyTyepException(String message) {
		super(message);
	}

	public UnknownPropertyTyepException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownPropertyTyepException(Throwable cause) {
		super(cause);
	}
}
