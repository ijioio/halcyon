package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.query.Condition.SimpleCondition;

public interface Expression<V> {

	public default Condition eq(V value) {
		return SimpleCondition.of(this, Operation.EQ, Value.of(value));
	}

	public default Condition eq(Expression<V> other) {
		return SimpleCondition.of(this, Operation.EQ, other);
	}
}