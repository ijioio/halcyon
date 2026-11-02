package com.ijioio.halcyon.core.query;

import java.util.List;

public final class Lower extends AbstractFunction<String> {

	public static Lower of(Expression<String> argument) {

		if (argument == null) {
			throw new IllegalArgumentException("argument must not be null");
		}

		return new Lower(List.of(argument));
	}

	private Lower(List<? extends Expression<?>> arguments) {
		super(arguments);
	}
}