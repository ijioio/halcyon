package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.ModelIndex;

public abstract class Table<I extends ModelIndex<?>> {

	private final Class<I> indexType;

	protected Table(Class<I> indexType) {
		this.indexType = indexType;
	}

	public Class<I> getIndexType() {
		return indexType;
	}
}