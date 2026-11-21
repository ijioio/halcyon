package com.ijioio.halcyon.processor.api.exception;

import com.ijioio.halcyon.processor.api.Logger.MessageContext;

public abstract class ProcessorException extends RuntimeException {

	private static final long serialVersionUID = 4210752250997414835L;

	private transient final MessageContext context;

	public ProcessorException(String message, MessageContext context) {

		super(message);

		this.context = context;
	}

	public ProcessorException(String message, MessageContext context, Throwable cause) {

		super(message, cause);

		this.context = context;
	}

	public MessageContext getContext() {
		return context;
	}
}