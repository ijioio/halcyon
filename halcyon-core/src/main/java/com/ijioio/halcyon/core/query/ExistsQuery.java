package com.ijioio.halcyon.core.query;

import java.util.List;
import java.util.stream.Stream;

import com.ijioio.halcyon.core.query.Join.JoinType;

public final class ExistsQuery {

	public static JoinOrWhereStep of(Table<?> table) {

		if (table == null) {
			throw new IllegalArgumentException("table must not be null");
		}

		return new JoinOrWhereStep(table, List.of());
	}

	private final Table<?> from;

	private final List<Join> joins;

	private final Condition where;

	private ExistsQuery(Table<?> from, List<Join> joins, Condition where) {

		this.from = from;
		this.joins = joins;
		this.where = where;
	}

	public Table<?> getFrom() {
		return from;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public Condition getWhere() {
		return where;
	}

	public static final class JoinOrWhereStep {

		private final Table<?> from;

		private final List<Join> joins;

		private JoinOrWhereStep(Table<?> from, List<Join> joins) {

			this.from = from;
			this.joins = joins;
		}

		public OnStep join(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep(from, joins, JoinType.INNER, table);
		}

		public OnStep leftJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep(from, joins, JoinType.LEFT, table);
		}

		public OnStep rightJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep(from, joins, JoinType.RIGHT, table);
		}

		public JoinOrWhereStep crossJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new JoinOrWhereStep(from,
					Stream.concat(joins.stream(), Stream.of(Join.of(JoinType.CROSS, table, null))).toList());
		}

		public BuildStep where(Condition condition) {

			if (condition == null) {
				throw new IllegalArgumentException("condition must not be null");
			}

			return new BuildStep(from, joins, condition);
		}

		public ExistsQuery build() {
			return new BuildStep(from, joins, null).build();
		}
	}

	public static final class OnStep {

		private final Table<?> from;

		private final List<Join> joins;

		private final JoinType joinType;

		private final Table<?> joinTable;

		private OnStep(Table<?> from, List<Join> joins, JoinType joinType, Table<?> joinTable) {

			this.from = from;
			this.joins = joins;
			this.joinType = joinType;
			this.joinTable = joinTable;
		}

		public JoinOrWhereStep on(Condition condition) {

			if (condition == null) {
				throw new IllegalArgumentException("condition must not be null");
			}

			return new JoinOrWhereStep(from,
					Stream.concat(joins.stream(), Stream.of(Join.of(joinType, joinTable, condition))).toList());
		}
	}

	public static final class BuildStep {

		private final Table<?> from;

		private final List<Join> joins;

		private final Condition where;

		private BuildStep(Table<?> from, List<Join> joins, Condition where) {

			this.from = from;
			this.where = where;
			this.joins = joins;
		}

		public ExistsQuery build() {
			return new ExistsQuery(from, joins, where);
		}
	}
}
