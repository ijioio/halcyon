package com.ijioio.halcyon.serialization.json;

import java.io.InputStream;
import java.io.OutputStream;

import com.ijioio.halcyon.serialization.SerializationHandler;
import com.ijioio.halcyon.serialization.exception.SerializationException;

import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.PrettyPrinter;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.DefaultPrettyPrinter;

public class JsonSerializationHandler implements SerializationHandler {

	private static final JsonFactory factory = JsonFactory.builder().build();

	private static final ObjectWriteContext writeContext = new ObjectWriteContext.Base() {

		@Override
		public PrettyPrinter getPrettyPrinter() {
			return new DefaultPrettyPrinter();
		}
	};

	private static final ObjectReadContext readContext = new ObjectReadContext.Base() {

	};

	@SuppressWarnings("unchecked")
	@Override
	public <T> void write(T object, OutputStream os) throws SerializationException {

		try {

			JsonSerializationContext context = JsonSerializationContext.of();

			try (JsonGenerator generator = factory.createGenerator(writeContext, os, JsonEncoding.UTF8)) {

				Class<T> type = (Class<T>) object.getClass();

				context.getValueHandler(type).write(object, generator, context);
			}

		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public <T> T read(Class<T> type, InputStream is) throws SerializationException {

		try {

			JsonSerializationContext context = JsonSerializationContext.of();

			try (JsonParser parser = factory.createParser(readContext, is)) {

				parser.nextToken();

				return context.getValueHandler(type).read(parser, context);
			}

		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}
}
