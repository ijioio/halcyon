package com.ijioio.halcyon.core.query;

public final class Functions {

	private Functions() {
	}

	public static Count count() {
		return Count.of();
	}

	public static Count count(Expression<?> argument) {
		return Count.of(argument);
	}

	public static Lower lower(Scalar<String> argument) {
		return Lower.of(argument);
	}

	public static Upper upper(Scalar<String> argument) {
		return Upper.of(argument);
	}

	@SafeVarargs
	public static <V> Coalesce<V> coalesce(Scalar<V>... arguments) {
		return Coalesce.of(arguments);
	}
}
