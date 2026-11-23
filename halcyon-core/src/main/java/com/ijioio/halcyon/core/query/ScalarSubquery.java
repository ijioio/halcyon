package com.ijioio.halcyon.core.query;

public final class ScalarSubquery<V> implements Scalar<V> {

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

	@Override
	public int getDegree() {
		return 1;
	}

	public SelectQuery<Record1<V>> getQuery() {
		return query;
	}
}