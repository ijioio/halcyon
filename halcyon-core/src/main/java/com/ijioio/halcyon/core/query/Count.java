package com.ijioio.halcyon.core.query;

import java.util.List;

public final class Count extends AbstractFunction<Long> implements Scalar<Long> {

	public static Count of() {
		return new Count(List.of());
	}

	public static Count of(Expression<?> argument) {

		if (argument == null) {
			throw new IllegalArgumentException("argument must not be null");
		}

		return new Count(List.of(argument));
	}

	private Count(List<Expression<?>> arguments) {
		super(arguments);
	}

	@Override
	public int getDegree() {
		return 1;
	}
}