package com.ijioio.halcyon.serialization.converter;

public interface Converter<T> {

	public String getType();

	public T convert(Class<?> type, T value) throws Exception;
}
