package com.ijioio.halcyon.core.query;

public final class ScalarSubquery<V> implements Expression<V> {

	public static <V> ScalarSubquery<V> of(SelectQuery<Record1<V>> query) {

		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}

		return new ScalarSubquery<>(query);
	}

	private final SelectQuery<Record1<V>> query;

	private ScalarSubquery(SelectQuery<Record1<V>> query) {
		this.query = query;
	}

	public SelectQuery<Record1<V>> getQuery() {
		return query;
	}
}