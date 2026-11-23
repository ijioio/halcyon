package com.ijioio.halcyon.core.query;

import java.util.Arrays;
import java.util.List;

public final class Coalesce<V> extends AbstractFunction<V> implements Scalar<V> {

	@SafeVarargs
	public static <V> Coalesce<V> of(Scalar<V>... arguments) {

		if (arguments == null) {
			throw new IllegalArgumentException("arguments must not be null");
		}

		if (arguments.length == 0) {
			throw new IllegalArgumentException("arguments must not be empty");
		}

		if (Arrays.stream(arguments).anyMatch(item -> item == null)) {
			throw new IllegalArgumentException("arguments must not contain null values");
		}

		if (Arrays.stream(arguments).map(item -> item.getDegree()).distinct().count() > 1) {
			throw new IllegalArgumentException("arguments must be of the same degree");
		}

		return new Coalesce<>(List.of(arguments));
	}

	private Coalesce(List<Scalar<V>> arguments) {
		super(arguments);
	}

	@Override
	public int getDegree() {
		return 1;
	}
}