package com.ijioio.halcyon.core.query;

import java.util.ArrayList;
import java.util.List;

public interface Condition {

	public default Condition and(Condition other) {

		if (other == null) {
			throw new IllegalArgumentException("other must not be null");
		}

		List<Condition> conditions = new ArrayList<>();

		if (this instanceof AndCondition and) {
			conditions.addAll(and.getConditions());
		} else {
			conditions.add(this);
		}

		if (other instanceof AndCondition and) {
			conditions.addAll(and.getConditions());
		} else {
			conditions.add(other);
		}

		return AndCondition.of(conditions);
	}

	public default Condition or(Condition other) {

		if (other == null) {
			throw new IllegalArgumentException("other must not be null");
		}

		List<Condition> conditions = new ArrayList<>();

		if (this instanceof OrCondition or) {
			conditions.addAll(or.getConditions());
		} else {
			conditions.add(this);
		}

		if (other instanceof OrCondition or) {
			conditions.addAll(or.getConditions());
		} else {
			conditions.add(other);
		}

		return OrCondition.of(conditions);
	}

	public static final class SimpleCondition implements Condition {

		public static <T> SimpleCondition of(Expression<T> left, Operation operation, Expression<T> right) {

			if (left == null) {
				throw new IllegalArgumentException("left must not be null");
			}

			if (operation == null) {
				throw new IllegalArgumentException("operation must not be null");
			}

			if (right == null) {
				throw new IllegalArgumentException("right must not be null");
			}

			if (left instanceof Row leftRow && right instanceof Row rightRow
					&& leftRow.getDegree() != rightRow.getDegree()) {
				throw new IllegalArgumentException("left and right must be of the same degree");
			}

			return new SimpleCondition(left, operation, right);
		}

		private final Expression<?> left;

		private final Operation operation;

		private final Expression<?> right;

		private SimpleCondition(Expression<?> left, Operation operation, Expression<?> right) {

			this.left = left;
			this.operation = operation;
			this.right = right;
		}

		public Expression<?> getLeft() {
			return left;
		}

		public Operation getOperation() {
			return operation;
		}

		public Expression<?> getRight() {
			return right;
		}
	}

	public static final class ExistsCondition implements Condition {

		public static ExistsCondition of(ExistsQuery query) {

			if (query == null) {
				throw new IllegalArgumentException("query must not be null");
			}

			return new ExistsCondition(query);
		}

		private final ExistsQuery query;

		private ExistsCondition(ExistsQuery query) {
			this.query = query;
		}

		public ExistsQuery getQuery() {
			return query;
		}
	}

	public static final class NotCondition implements Condition {

		public static NotCondition of(Condition condition) {

			if (condition == null) {
				throw new IllegalArgumentException("condition must not be null");
			}

			return new NotCondition(condition);
		}

		private final Condition condition;

		private NotCondition(Condition condition) {
			this.condition = condition;
		}

		public Condition getCondition() {
			return condition;
		}
	}

	public static final class AndCondition implements Condition {

		public static AndCondition of(List<Condition> conditions) {

			if (conditions == null) {
				throw new IllegalArgumentException("conditions must not be null");
			}

			if (conditions.size() == 0) {
				throw new IllegalArgumentException("conditions must not be empty");
			}

			if (conditions.stream().anyMatch(item -> item == null)) {
				throw new IllegalArgumentException("conditions must not contain null values");
			}

			return new AndCondition(conditions);
		}

		private final List<Condition> conditions;

		private AndCondition(List<Condition> conditions) {
			this.conditions = List.copyOf(conditions);
		}

		public List<Condition> getConditions() {
			return conditions;
		}
	}

	public static final class OrCondition implements Condition {

		public static OrCondition of(List<Condition> conditions) {

			if (conditions == null) {
				throw new IllegalArgumentException("conditions must not be null");
			}

			if (conditions.size() == 0) {
				throw new IllegalArgumentException("conditions must not be empty");
			}

			if (conditions.stream().anyMatch(item -> item == null)) {
				throw new IllegalArgumentException("conditions must not contain null values");
			}

			return new OrCondition(conditions);
		}

		private final List<Condition> conditions;

		private OrCondition(List<Condition> conditions) {
			this.conditions = List.copyOf(conditions);
		}

		public List<Condition> getConditions() {
			return conditions;
		}
	}
}