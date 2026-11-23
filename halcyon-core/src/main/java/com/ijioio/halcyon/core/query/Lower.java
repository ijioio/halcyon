package com.ijioio.halcyon.core.query;

import java.util.List;

public final class Lower extends AbstractFunction<String> implements Scalar<String> {

	public static Lower of(Scalar<String> argument) {

		if (argument == null) {
			throw new IllegalArgumentException("argument must not be null");
		}

		return new Lower(List.of(argument));
	}

	private Lower(List<Scalar<?>> arguments) {
		super(arguments);
	}

	@Override
	public int getDegree() {
		return 1;
	}
}