package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.query.Condition.ExistsCondition;
import com.ijioio.halcyon.core.query.Condition.NotCondition;

public final class Conditions {

	private Conditions() {
	}

	public static Condition not(Condition condition) {
		return NotCondition.of(condition);
	}

	public static Condition exists(ExistsQuery query) {
		return ExistsCondition.of(query);
	}
}