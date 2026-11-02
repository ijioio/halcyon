package com.ijioio.halcyon.core.query;

public final class Select<R extends Record> {

	public static <R extends Record> Select<R> of(R record, boolean distinct) {

		if (record == null) {
			throw new IllegalArgumentException("record must not be null");
		}

		return new Select<>(record, distinct);
	}

	private final R record;

	private final boolean distinct;

	private Select(R record, boolean distinct) {

		this.record = record;
		this.distinct = distinct;
	}

	public R getRecord() {
		return record;
	}

	public boolean isDistinct() {
		return distinct;
	}
}
