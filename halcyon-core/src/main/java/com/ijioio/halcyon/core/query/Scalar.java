package com.ijioio.halcyon.core.query;

public interface Scalar<V> extends Expression<V> {

	public default Condition eq(V value) {
		return eq(Values.of(value));
	}

	public default Condition eq(Scalar<V> other) {
		return Conditions.eq(this, other);
	}
}