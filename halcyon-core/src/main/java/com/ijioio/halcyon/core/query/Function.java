package com.ijioio.halcyon.core.query;

import java.util.List;

public interface Function<V> extends Expression<V> {

	public List<Expression<?>> getArguments();
}