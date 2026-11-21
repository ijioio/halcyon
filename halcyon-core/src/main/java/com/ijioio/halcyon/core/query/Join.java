package com.ijioio.halcyon.core.query;

public final class Join {

	public static Join of(JoinType type, Table<?> table, Condition condition) {

		if (type == null) {
			throw new IllegalArgumentException("type must not be null");
		}

		if (table == null) {
			throw new IllegalArgumentException("table must not be null");
		}

		if (condition == null && type != JoinType.CROSS) {
			throw new IllegalArgumentException("condition must not be null");
		}

		return new Join(type, table, condition);
	}

	private final JoinType type;

	private final Table<?> table;

	private final Condition condition;

	private Join(JoinType type, Table<?> table, Condition condition) {

		this.type = type;
		this.table = table;
		this.condition = condition;
	}

	public JoinType getType() {
		return type;
	}

	public Table<?> getTable() {
		return table;
	}

	public Condition getCondition() {
		return condition;
	}

	public static enum JoinType {

		INNER,
		LEFT,
		RIGHT,
		CROSS;
	}
}
