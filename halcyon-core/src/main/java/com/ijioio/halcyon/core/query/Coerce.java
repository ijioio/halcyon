package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.TypeReference;

public final class Coerce<V> implements Scalar<V> {

	public static <V> Coerce<V> of(Expression<?> expression, TypeReference<V> type) {

		if (expression == null) {
			throw new IllegalArgumentException("expression must not be null");
		}

		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}

		return new Coerce<>(expression, type);
	}

	private final Expression<?> expression;

	private final TypeReference<V> type;

	private Coerce(Expression<?> expression, TypeReference<V> type) {

		this.expression = expression;
		this.type = type;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	public Expression<?> getExpression() {
		return expression;
	}

	public TypeReference<V> getType() {
		return type;
	}
}