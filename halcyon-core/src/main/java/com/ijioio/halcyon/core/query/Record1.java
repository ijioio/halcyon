package com.ijioio.halcyon.core.query;

public final class Record1<T1> implements Record {

	public static <T1> Record1<T1> of(Expression<T1> expression1, T1 value1) {

		if (expression1 == null) {
			throw new IllegalArgumentException("expression1 must not be null");
		}

		return new Record1<>(expression1, value1);
	}

	private final Expression<T1> expression1;

	private final T1 value1;

	private Record1(Expression<T1> expression1, T1 value1) {

		this.expression1 = expression1;
		this.value1 = value1;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	public Expression<T1> getExpression1() {
		return expression1;
	}

	public T1 getValue1() {
		return value1;
	}
}
