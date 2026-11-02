package com.ijioio.halcyon.core.query;

public final class Functions {

	private Functions() {
	}

	public static Function<Long> count() {
		return Count.of();
	}

	public static Function<Long> count(Expression<?> argument) {
		return Count.of(argument);
	}

	public static Function<String> lower(Expression<String> argument) {
		return Lower.of(argument);
	}

	public static Function<String> upper(Expression<String> argument) {
		return Upper.of(argument);
	}

	@SafeVarargs
	public static <V> Function<V> coalesce(Expression<V>... arguments) {
		return Coalesce.of(arguments);
	}
}
