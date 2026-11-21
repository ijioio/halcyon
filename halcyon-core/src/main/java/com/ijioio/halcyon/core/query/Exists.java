package com.ijioio.halcyon.core.query;

public final class Exists implements Expression<Boolean> {

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

	public ExistsQuery getQuery() {
		return query;
	}
}