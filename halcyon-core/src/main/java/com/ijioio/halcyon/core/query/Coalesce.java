package com.ijioio.halcyon.core.query;

import java.util.Arrays;
import java.util.List;

public final class Coalesce<V> extends AbstractFunction<V> {

	@SafeVarargs
	public static <V> Coalesce<V> of(Expression<V>... arguments) {

		if (arguments == null) {
			throw new IllegalArgumentException("arguments must not be null");
		}

		if (arguments.length == 0) {
			throw new IllegalArgumentException("arguments must not be empty");
		}

		if (Arrays.stream(arguments).anyMatch(item -> item == null)) {
			throw new IllegalArgumentException("arguments must not contain null values");
		}

		return new Coalesce<>(List.of(arguments));
	}

	private Coalesce(List<? extends Expression<V>> arguments) {
		super(arguments);
	}
}