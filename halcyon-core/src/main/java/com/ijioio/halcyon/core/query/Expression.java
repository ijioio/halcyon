package com.ijioio.halcyon.core.query;

import java.util.List;

import com.ijioio.halcyon.core.TypeReference;

public interface Expression<V> {

	public int getDegree();

	public default List<TypeReference<?>> getTypes() {
		return List.of();
	}
}