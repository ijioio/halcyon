package com.ijioio.halcyon.core.query;

import java.util.Arrays;

public final class Rows {

	private Rows() {
	}

	public static <T1> Row1<T1> of(T1 value1) {
		return Row1.of(Value.of(value1));
	}

	public static <T1> Row1<T1> of(Expression<T1> expression1) {
		return Row1.of(expression1);
	}

	public static <T1, T2> Row2<T1, T2> of(T1 value1, T2 value2) {
		return Row2.of(Value.of(value1), Value.of(value2));
	}

	public static <T1, T2> Row2<T1, T2> of(Expression<T1> expression1, Expression<T2> expression2) {
		return Row2.of(expression1, expression2);
	}

	public static RowN of(Object... values) {
		return RowN.of(Arrays.stream(values).map(item -> Value.of(item)).toArray(size -> new Expression<?>[size]));
	}

	public static RowN of(Expression<?>... expressions) {
		return RowN.of(expressions);
	}
}
