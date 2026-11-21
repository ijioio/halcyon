package com.ijioio.halcyon.core.query;

public final class Row2<T1, T2> implements Row, Expression<Record2<T1, T2>> {

	public static <T1, T2> Row2<T1, T2> of(Expression<T1> expression1, Expression<T2> expression2) {

		if (expression1 == null) {
			throw new IllegalArgumentException("expression1 must not be null");
		}

		if (expression2 == null) {
			throw new IllegalArgumentException("expression2 must not be null");
		}

		return new Row2<>(expression1, expression2);
	}

	private final Expression<T1> expression1;

	private final Expression<T2> expression2;

	private Row2(Expression<T1> expression1, Expression<T2> expression2) {

		this.expression1 = expression1;
		this.expression2 = expression2;
	}

	@Override
	public int getDegree() {
		return 2;
	}

	public Expression<T1> getExpression1() {
		return expression1;
	}

	public Expression<T2> getExpression2() {
		return expression2;
	}
}
