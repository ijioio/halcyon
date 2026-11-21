package com.ijioio.halcyon.core.exception;

public class IntrospectionException extends Exception {

	private static final long serialVersionUID = -5101124713961347179L;

	public IntrospectionException(final String message) {
		super(message);
	}

	public IntrospectionException(final Throwable cause) {
		super(cause);
	}

	public IntrospectionException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
