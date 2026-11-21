package com.ijioio.halcyon.processor.api.exception;

import com.ijioio.halcyon.processor.api.Logger.MessageContext;

public class GenerateException extends ProcessorException {

	private static final long serialVersionUID = 7375009282859227173L;

	public GenerateException(String message, MessageContext context) {
		super(message, context);
	}

	public GenerateException(String message, MessageContext context, Throwable cause) {
		super(message, context, cause);
	}
}