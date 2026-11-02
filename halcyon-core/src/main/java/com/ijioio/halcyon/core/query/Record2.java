package com.ijioio.halcyon.core.query;

public final class Record2<T1, T2> implements Record {

	public static <T1, T2> Record2<T1, T2> of(Expression<T1> expression1, Expression<T2> expression2, T1 value1,
			T2 value2) {

		if (expression1 == null) {
			throw new IllegalArgumentException("expression1 must not be null");
		}

		if (expression2 == null) {
			throw new IllegalArgumentException("expression2 must not be null");
		}

		return new Record2<>(expression1, expression2, value1, value2);
	}

	private final Expression<T1> expression1;

	private final Expression<T2> expression2;

	private final T1 value1;

	private final T2 value2;

	private Record2(Expression<T1> expression1, Expression<T2> expression2, T1 value1, T2 value2) {

		this.expression1 = expression1;
		this.expression2 = expression2;
		this.value1 = value1;
		this.value2 = value2;
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

	public T1 getValue1() {
		return value1;
	}

	public T2 getValue2() {
		return value2;
	}
}
