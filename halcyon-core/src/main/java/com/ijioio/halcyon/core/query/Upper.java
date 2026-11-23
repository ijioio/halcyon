package com.ijioio.halcyon.core.query;

import java.util.List;

public final class Upper extends AbstractFunction<String> implements Scalar<String> {

	public static Upper of(Scalar<String> argument) {

		if (argument == null) {
			throw new IllegalArgumentException("argument must not be null");
		}

		return new Upper(List.of(argument));
	}

	private Upper(List<Scalar<?>> arguments) {
		super(arguments);
	}

	@Override
	public int getDegree() {
		return 1;
	}
}