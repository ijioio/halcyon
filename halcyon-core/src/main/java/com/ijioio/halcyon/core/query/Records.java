package com.ijioio.halcyon.core.query;

import java.util.Collections;
import java.util.List;

public final class Records {

	private Records() {
	}

	public static <T1> Record1<T1> of(Expression<T1> expression1) {
		return Record1.of(expression1, null);
	}

	public static <T1, T2> Record2<T1, T2> of(Expression<T1> expression1, Expression<T2> expression2) {
		return Record2.of(expression1, expression2, null, null);
	}

	public static RecordN of(Expression<?>... expressions) {
		return RecordN.of(List.of(expressions), Collections.nCopies(expressions.length, null));
	}
}
