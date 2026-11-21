package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.property.Property;

public final class Field<T extends Table<?>, V> implements Expression<V> {

	public static <T extends Table<?>, V> Field<T, V> of(T table, Property<V> property) {
		return new Field<>(table, property);
	}

	private final T table;

	protected final Property<V> property;

	private Field(T table, Property<V> property) {

		this.table = table;
		this.property = property;
	}

	public T getTable() {
		return table;
	}

	public Property<V> getProperty() {
		return property;
	}
}