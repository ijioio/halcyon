package com.ijioio.halcyon.serialization;

import java.io.InputStream;
import java.io.OutputStream;

import com.ijioio.halcyon.serialization.exception.SerializationException;

public interface SerializationHandler {

	public <T> void write(T object, OutputStream os) throws SerializationException;

	public <T> T read(Class<T> type, InputStream is) throws SerializationException;
}
