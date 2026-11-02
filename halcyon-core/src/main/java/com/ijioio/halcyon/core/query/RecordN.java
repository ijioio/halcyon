package com.ijioio.halcyon.core.query;

import java.util.List;

public final class RecordN implements Record {

	public static RecordN of(List<Expression<?>> expressions, List<?> values) {

		if (expressions == null) {
			throw new IllegalArgumentException("expressions must not be null");
		}

		if (expressions.size() == 0) {
			throw new IllegalArgumentException("expressions must not be empty");
		}

		if (expressions.stream().anyMatch(item -> item == null)) {
			throw new IllegalArgumentException("expressions must not contain null");
		}

		if (values == null) {
			throw new IllegalArgumentException("values must not be null");
		}

		if (values.size() == 0) {
			throw new IllegalArgumentException("values must not be empty");
		}

		if (expressions.size() != values.size()) {
			throw new IllegalArgumentException("expressions and values must be of the same degree");
		}

		return new RecordN(expressions, values);
	}

	private final List<Expression<?>> expressions;

	private final List<?> values;

	private RecordN(List<Expression<?>> expressions, List<?> values) {

		this.expressions = List.copyOf(expressions);
		this.values = List.copyOf(values);
	}

	@Override
	public int getDegree() {
		return getExpressions().size();
	}

	public List<Expression<?>> getExpressions() {
		return expressions;
	}

	public List<?> getValues() {
		return values;
	}
}