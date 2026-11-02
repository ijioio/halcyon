package com.ijioio.halcyon.processor.api.exception;

import javax.lang.model.type.TypeMirror;

public class DeferredElementException extends RuntimeException {

	private static final long serialVersionUID = 6376442868162744695L;

	private transient final TypeMirror type;

	public DeferredElementException(TypeMirror type) {
		this(null, type);
	}

	public DeferredElementException(String message, TypeMirror type) {

		super(message);

		this.type = type;
	}

	public TypeMirror getType() {
		return type;
	}
}