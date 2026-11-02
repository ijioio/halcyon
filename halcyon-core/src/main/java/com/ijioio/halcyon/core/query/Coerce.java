package com.ijioio.halcyon.core.query;

public final class Coerce<V> implements Expression<V> {

	public static <V> Coerce<V> of(Expression<?> expression) {

		if (expression == null) {
			throw new IllegalArgumentException("expression must not be null");
		}

		return new Coerce<>(expression);
	}

	private final Expression<?> expression;

	private Coerce(Expression<?> expression) {
		this.expression = expression;
	}

	public Expression<?> getExpression() {
		return expression;
	}
}