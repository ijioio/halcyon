package com.ijioio.halcyon.core.query;

public final class Values {

	private Values() {
	}

	public static <V> Value<V> of(V value) {
		return Value.of(value);
	}
}
