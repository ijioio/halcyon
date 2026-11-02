package com.ijioio.halcyon.core;

import java.util.Collection;

import com.ijioio.halcyon.core.exception.IntrospectionException;
import com.ijioio.halcyon.core.property.Property;

public interface Introspectable {

	public <T> T getValue(Property<T> property) throws IntrospectionException;

	public <T> void setValue(Property<T> property, T value) throws IntrospectionException;

	public Collection<Property<?>> getProperties();
}
