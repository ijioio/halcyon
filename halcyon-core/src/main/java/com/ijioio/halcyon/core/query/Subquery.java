package com.ijioio.halcyon.core.query;

public final class Subquery<R extends Record> implements Expression<R> {

	public static <R extends Record> Subquery<R> of(SelectQuery<R> query) {

		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}

		return new Subquery<>(query);
	}

	private final SelectQuery<R> query;

	private Subquery(SelectQuery<R> query) {
		this.query = query;
	}

	public SelectQuery<R> getQuery() {
		return query;
	}
}