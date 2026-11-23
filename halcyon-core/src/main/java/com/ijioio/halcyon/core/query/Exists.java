package com.ijioio.halcyon.core.query;

public final class Exists implements Scalar<Boolean> {

	public static Exists of(ExistsQuery query) {

		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}

		return new Exists(query);
	}

	private final ExistsQuery query;

	private Exists(ExistsQuery query) {
		this.query = query;
	}

	@Override
	public int getDegree() {
		return 1;
	}

	public ExistsQuery getQuery() {
		return query;
	}
}