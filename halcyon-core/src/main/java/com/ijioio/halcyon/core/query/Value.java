package com.ijioio.halcyon.core.query;

public final class Value<V> implements Expression<V> {

	public static <V> Value<V> of(V value) {
		return new Value<>(value);
	}

	private final V value;

	private Value(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}
}