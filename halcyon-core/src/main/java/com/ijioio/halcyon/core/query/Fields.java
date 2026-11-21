package com.ijioio.halcyon.core.query;

import com.ijioio.halcyon.core.property.Property;

public final class Fields {

	private Fields() {
	}

	public static <T extends Table<?>, V> Field<T, V> of(T table, Property<V> property) {
		return Field.of(table, property);
	}
}
