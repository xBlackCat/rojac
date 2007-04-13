package org.xblackcat.sunaj.service.soap;

/**
 * Date: 10 квіт 2007
 *
 * @author Alexey
 */

public class JanusServiceException extends Exception {
	public JanusServiceException() {
	}

	public JanusServiceException(Throwable cause) {
		super(cause);
	}

	public JanusServiceException(String message) {
		super(message);
	}

	public JanusServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
