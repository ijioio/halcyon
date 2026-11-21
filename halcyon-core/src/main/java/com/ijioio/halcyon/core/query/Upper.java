package com.ijioio.halcyon.core.query;

import java.util.List;

public final class Upper extends AbstractFunction<String> {

	public static Upper of(Expression<String> argument) {

		if (argument == null) {
			throw new IllegalArgumentException("argument must not be null");
		}

		return new Upper(List.of(argument));
	}

	private Upper(List<? extends Expression<?>> arguments) {
		super(arguments);
	}
}