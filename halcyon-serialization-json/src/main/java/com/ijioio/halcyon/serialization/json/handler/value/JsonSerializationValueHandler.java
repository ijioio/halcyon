package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;

public interface JsonSerializationValueHandler<T> {

	public static final class Attributes {

		public static String TYPE = "@type";

		public static String LINK = "@link";
	}

	public Class<T> getType();

	public void write(T value, JsonGenerator generator, JsonSerializationContext context) throws Exception;

	public T read(JsonParser parser, JsonSerializationContext context) throws Exception;

	@SuppressWarnings({ "unchecked" })
	public default void writeObject(Object value, JsonGenerator generator, JsonSerializationContext context)
			throws Exception {

		generator.writeStartObject();

		generator.writeName(Attributes.TYPE);
		context.getValueHandler(Class.class).write(value.getClass(), generator, context);

		generator.writeName("value");

		((JsonSerializationValueHandler<Object>) context.getValueHandler(value.getClass())).write(value, generator,
				context);

		generator.writeEndObject();
	}

	@SuppressWarnings("unchecked")
	public default Object readObject(JsonParser parser, JsonSerializationContext context) throws Exception {

		if (parser.currentToken() != JsonToken.START_OBJECT) {
			throw new SerializationException("corrupted data: object must be an object");
		}

		parser.nextToken();

		if (!parser.currentName().equals(Attributes.TYPE)) {
			throw new SerializationException(String.format("corrupted data: %s not found", Attributes.TYPE));
		}

		parser.nextToken();

		Class<?> type = context.getValueHandler(Class.class).read(parser, context);

		parser.nextToken();

		if (!parser.currentName().equals("value")) {
			throw new SerializationException("corrupted data: value not found");
		}

		parser.nextToken();

		Object object = ((JsonSerializationValueHandler<Object>) context.getValueHandler(type)).read(parser, context);

		while (parser.nextToken() != JsonToken.END_OBJECT) {

			parser.nextToken();
			parser.skipChildren();
		}

		return object;
	}
}
