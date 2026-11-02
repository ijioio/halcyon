package com.ijioio.halcyon.core.query;

public final class Row1<T1> implements Row, Expression<Record1<T1>> {

	public static <T1> Row1<T1> of(Expression<T1> expression1) {

		if (expression1 == null) {
			throw new IllegalArgumentException("expression1 must not be null");
		}

		return new Row1<>(expression1);
	}

	private final Expression<T1> expression1;

	private Row1(Expression<T1> expression1) {
		this.expression1 = expression1;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	public Expression<T1> getExpression1() {
		return expression1;
	}
}
