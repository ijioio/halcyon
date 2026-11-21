package com.ijioio.halcyon.core.property;

import java.util.Objects;

import com.ijioio.halcyon.core.TypeReference;

public class Property<T> {

	public static <T> Property<T> of(String name, TypeReference<T> type) {
		return new Property<T>(name, type);
	}

	private final String name;

	private final TypeReference<T> type;

	private Property(String name, TypeReference<T> type) {

		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public TypeReference<T> getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Property<?> other = (Property<?>) obj;

		return Objects.equals(name, other.name) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", type=" + type + "]";
	}
}
