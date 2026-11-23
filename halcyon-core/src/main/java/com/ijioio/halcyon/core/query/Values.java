package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.TypeReference;

public final class Values {

	private Values() {
	}

	public static <V> Value<V> of(V value) {
		return of(value, (TypeReference<V>) null);
	}

	public static <V> Value<V> of(V value, Class<V> type) {
		return of(value, TypeReference.of(type));
	}

	public static <V> Value<V> of(V value, TypeReference<V> type) {
		return Value.of(value, type);
	}
}
