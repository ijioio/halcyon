package com.ijioio.halcyon.core.query;

import java.util.List;

public abstract class AbstractFunction<V> implements Function<V> {

	private final List<Expression<?>> arguments;

	protected AbstractFunction(List<? extends Expression<?>> arguments) {
		this.arguments = List.copyOf(arguments);
	}

	@Override
	public List<Expression<?>> getArguments() {
		return arguments;
	}
}