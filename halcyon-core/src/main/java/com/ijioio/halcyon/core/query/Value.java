package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.TypeReference;

public final class Value<V> implements Scalar<V> {

	public static <V> Value<V> of(V value, TypeReference<V> type) {
		return new Value<>(value, type);
	}

	private final V value;

	private final TypeReference<V> type;

	private Value(V value, TypeReference<V> type) {

		this.value = value;
		this.type = type;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	public V getValue() {
		return value;
	}

	public TypeReference<V> getType() {
		return type;
	}
}