package com.ijioio.halcyon.serialization.exception;

public class SerializationException extends Exception {

	private static final long serialVersionUID = 2748154070222978795L;

	public SerializationException(final String message) {
		super(message);
	}

	public SerializationException(final Throwable cause) {
		super(cause);
	}

	public SerializationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
