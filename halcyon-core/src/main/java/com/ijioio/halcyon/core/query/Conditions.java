package com.ijioio.halcyon.core.query;

import java.util.List;

import com.ijioio.halcyon.core.query.Condition.AndCondition;
import com.ijioio.halcyon.core.query.Condition.ExistsCondition;
import com.ijioio.halcyon.core.query.Condition.NotCondition;
import com.ijioio.halcyon.core.query.Condition.OrCondition;
import com.ijioio.halcyon.core.query.Condition.SimpleCondition;

public final class Conditions {

	private Conditions() {
	}

	public static <T> SimpleCondition eq(Expression<T> left, Expression<T> right) {
		return SimpleCondition.of(left, Operation.EQ, right);
	}

	public static AndCondition and(List<Condition> conditions) {
		return AndCondition.of(conditions);
	}

	public static OrCondition or(List<Condition> conditions) {
		return OrCondition.of(conditions);
	}

	public static NotCondition not(Condition condition) {
		return NotCondition.of(condition);
	}

	public static ExistsCondition exists(ExistsQuery query) {
		return ExistsCondition.of(query);
	}
}