package com.ijioio.halcyon.core.query;

import java.util.Arrays;
import java.util.List;

public final class RowN implements Row, Expression<RecordN> {

	public static RowN of(Expression<?>... expressions) {

		if (expressions == null) {
			throw new IllegalArgumentException("expressions must not be null");
		}

		if (expressions.length == 0) {
			throw new IllegalArgumentException("expressions must not be empty");
		}

		if (Arrays.stream(expressions).anyMatch(item -> item == null)) {
			throw new IllegalArgumentException("expressions must not contain null values");
		}

		return new RowN(List.of(expressions));
	}

	private final List<Expression<?>> expressions;

	private RowN(List<Expression<?>> expressions) {
		this.expressions = List.copyOf(expressions);
	}

	@Override
	public int getDegree() {
		return getExpressions().size();
	}

	public List<Expression<?>> getExpressions() {
		return expressions;
	}
}
