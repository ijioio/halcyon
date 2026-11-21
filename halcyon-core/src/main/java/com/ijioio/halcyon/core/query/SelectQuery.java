package com.ijioio.halcyon.core.query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.ijioio.halcyon.core.query.Join.JoinType;

public final class SelectQuery<R extends Record> {

	public static SelectStep of(Table<?> table) {

		if (table == null) {
			throw new IllegalArgumentException("table must not be null");
		}

		return new SelectStep(table);
	}

	private final Table<?> from;

	private final Select<R> select;

	private final List<Join> joins;

	private final Condition where;

	private SelectQuery(Table<?> from, Select<R> select, List<Join> joins, Condition where) {

		this.from = from;
		this.select = select;
		this.joins = joins;
		this.where = where;
	}

	public Table<?> getFrom() {
		return from;
	}

	public Select<R> getSelect() {
		return select;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public Condition getWhere() {
		return where;
	}

	public static final class SelectStep {

		private final Table<?> from;

		private SelectStep(Table<?> from) {
			this.from = from;
		}

		public <T1> JoinOrWhereStep<Record1<T1>> select(Expression<T1> expression1) {

			if (expression1 == null) {
				throw new IllegalArgumentException("expression1 must not be null");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expression1), false), List.of());
		}

		public <T1, T2> JoinOrWhereStep<Record2<T1, T2>> select(Expression<T1> expression1,
				Expression<T2> expression2) {

			if (expression1 == null) {
				throw new IllegalArgumentException("expression1 must not be null");
			}

			if (expression2 == null) {
				throw new IllegalArgumentException("expression2 must not be null");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expression1, expression2), false), List.of());
		}

		public JoinOrWhereStep<RecordN> select(Expression<?>... expressions) {

			if (expressions == null) {
				throw new IllegalArgumentException("expressions must not be null");
			}

			if (expressions.length == 0) {
				throw new IllegalArgumentException("expressions must not be empty");
			}

			if (Arrays.stream(expressions).anyMatch(item -> item == null)) {
				throw new IllegalArgumentException("expressions must not contain null values");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expressions), false), List.of());
		}

		public <T1> JoinOrWhereStep<Record1<T1>> selectDistinct(Expression<T1> expression) {

			if (expression == null) {
				throw new IllegalArgumentException("expression must not be null");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expression), true), List.of());
		}

		public <T1, T2> JoinOrWhereStep<Record2<T1, T2>> selectDistinct(Expression<T1> expression1,
				Expression<T2> expression2) {

			if (expression1 == null) {
				throw new IllegalArgumentException("expression1 must not be null");
			}

			if (expression2 == null) {
				throw new IllegalArgumentException("expression2 must not be null");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expression1, expression2), true), List.of());
		}

		public JoinOrWhereStep<RecordN> selectDistinct(Expression<?>... expressions) {

			if (expressions == null) {
				throw new IllegalArgumentException("expressions must not be null");
			}

			if (expressions.length == 0) {
				throw new IllegalArgumentException("expressions must not be empty");
			}

			if (Arrays.stream(expressions).anyMatch(item -> item == null)) {
				throw new IllegalArgumentException("expressions must not contain null values");
			}

			return new JoinOrWhereStep<>(from, Select.of(Records.of(expressions), true), List.of());
		}
	}

	public static final class JoinOrWhereStep<R extends Record> {

		private final Table<?> from;

		private final Select<R> select;

		private final List<Join> joins;

		private JoinOrWhereStep(Table<?> from, Select<R> select, List<Join> joins) {

			this.from = from;
			this.select = select;
			this.joins = joins;
		}

		public OnStep<R> join(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep<>(from, select, joins, JoinType.INNER, table);
		}

		public OnStep<R> leftJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep<>(from, select, joins, JoinType.LEFT, table);
		}

		public OnStep<R> rightJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new OnStep<>(from, select, joins, JoinType.RIGHT, table);
		}

		public JoinOrWhereStep<R> crossJoin(Table<?> table) {

			if (table == null) {
				throw new IllegalArgumentException("table must not be null");
			}

			return new JoinOrWhereStep<>(from, select,
					Stream.concat(joins.stream(), Stream.of(Join.of(JoinType.CROSS, table, null))).toList());
		}

		public BuildStep<R> where(Condition condition) {

			if (condition == null) {
				throw new IllegalArgumentException("condition must not be null");
			}

			return new BuildStep<>(from, select, joins, condition);
		}

		public SelectQuery<R> build() {
			return new BuildStep<>(from, select, joins, null).build();
		}
	}

	public static final class OnStep<R extends Record> {

		private final Table<?> from;

		private final Select<R> select;

		private final List<Join> joins;

		private final JoinType joinType;

		private final Table<?> joinTable;

		private OnStep(Table<?> from, Select<R> select, List<Join> joins, JoinType joinType, Table<?> joinTable) {

			this.from = from;
			this.select = select;
			this.joins = joins;
			this.joinType = joinType;
			this.joinTable = joinTable;
		}

		public JoinOrWhereStep<R> on(Condition condition) {

			if (condition == null) {
				throw new IllegalArgumentException("condition must not be null");
			}

			return new JoinOrWhereStep<>(from, select,
					Stream.concat(joins.stream(), Stream.of(Join.of(joinType, joinTable, condition))).toList());
		}
	}

	public static final class BuildStep<R extends Record> {

		private final Table<?> from;

		private final Select<R> select;

		private final List<Join> joins;

		private final Condition where;

		private BuildStep(Table<?> from, Select<R> select, List<Join> joins, Condition where) {

			this.from = from;
			this.select = select;
			this.where = where;
			this.joins = joins;
		}

		public SelectQuery<R> build() {
			return new SelectQuery<>(from, select, joins, where);
		}
	}
}
