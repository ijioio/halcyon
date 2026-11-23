package com.ijioio.halcyon.core.query;

public interface Composite<R extends Record> extends Expression<R> {

	public default Condition eq(Composite<R> other) {
		return Conditions.eq(this, other);
	}
}