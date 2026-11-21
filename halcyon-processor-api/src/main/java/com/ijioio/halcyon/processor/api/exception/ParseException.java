package com.ijioio.halcyon.processor.api.exception;

import com.ijioio.halcyon.processor.api.Logger.MessageContext;

public class ParseException extends ProcessorException {

	private static final long serialVersionUID = -3444886390458238378L;

	public ParseException(String message, MessageContext context) {
		super(message, context);
	}

	public ParseException(String message, MessageContext context, Throwable cause) {
		super(message, context, cause);
	}
}