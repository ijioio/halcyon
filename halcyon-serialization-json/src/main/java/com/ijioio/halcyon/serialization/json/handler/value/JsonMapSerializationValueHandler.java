package com.ijioio.halcyon.serialization.json.handler.value;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ijioio.halcyon.serialization.converter.Converter;
import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;

@SuppressWarnings("rawtypes")
public class JsonMapSerializationValueHandler implements JsonSerializationValueHandler<Map> {

	@Override
	public Class<Map> getType() {
		return Map.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Map value, JsonGenerator generator, JsonSerializationContext context) throws Exception {

		generator.writeStartObject();

		generator.writeName(Attributes.TYPE);
		context.getValueHandler(Class.class).write(value.getClass(), generator, context);

		generator.writeName("entries");
		generator.writeStartArray();

		for (Entry<?, ?> entry : (Set<Entry<?, ?>>) value.entrySet()) {

			generator.writeStartObject();

			generator.writeName("key");

			if (entry.getKey() != null) {
				writeObject(entry.getKey(), generator, context);
			} else {
				generator.writeNull();
			}

			generator.writeName("value");

			if (entry.getValue() != null) {
				writeObject(entry.getValue(), generator, context);
			} else {
				generator.writeNull();
			}

			generator.writeEndObject();
		}

		generator.writeEndArray();
		generator.writeEndObject();
	}

	@Override
	public Map read(JsonParser parser, JsonSerializationContext context) throws Exception {

		if (parser.currentToken() != JsonToken.START_OBJECT) {
			throw new SerializationException("corrupted data: map must be an object");
		}

		parser.nextToken();

		if (!parser.currentName().equals(Attributes.TYPE)) {
			throw new SerializationException(String.format("corrupted data: %s not found", Attributes.TYPE));
		}

		parser.nextToken();

		Class<?> type = context.getValueHandler(Class.class).read(parser, context);

		parser.nextToken();

		if (!parser.currentName().equals("entries")) {
			throw new SerializationException("corrupted data: entries not found");
		}

		parser.nextToken();

		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new SerializationException("corrupted data: entries must be an array");
		}

		Map<Object, Object> map = new LinkedHashMap<>();

		while (parser.nextToken() != JsonToken.END_ARRAY) {

			if (parser.currentToken() != JsonToken.START_OBJECT) {
				throw new SerializationException("corrupted data: entry must be an object");
			}

			parser.nextToken();

			if (!parser.currentName().equals("key")) {
				throw new SerializationException("corrupted data: key not found");
			}

			parser.nextToken();

			Object key = parser.currentToken() != JsonToken.VALUE_NULL ? readObject(parser, context) : null;

			parser.nextToken();

			if (!parser.currentName().equals("value")) {
				throw new SerializationException("corrupted data: value not found");
			}

			parser.nextToken();

			Object value = parser.currentToken() != JsonToken.VALUE_NULL ? readObject(parser, context) : null;

			while (parser.nextToken() != JsonToken.END_OBJECT) {

				parser.nextToken();
				parser.skipChildren();
			}

			map.put(key, value);
		}

		while (parser.nextToken() != JsonToken.END_OBJECT) {

			parser.nextToken();
			parser.skipChildren();
		}

		return convert(type, map, context);
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> convert(Class<?> type, Map<Object, Object> map, JsonSerializationContext context)
			throws Exception {

		if (map.getClass().equals(type)) {
			return map;
		}

		Constructor<?> constructor = null;

		try {
			constructor = type.getConstructor();
		} catch (NoSuchMethodException e) {
			// Ignore
		}

		if (constructor != null && constructor.canAccess(null)) {

			Map<Object, Object> instance = (Map<Object, Object>) constructor.newInstance();

			instance.putAll(map);

			return instance;
		}

		Converter<Map<Object, Object>> converter = context.getConverter(type.getName());

		return converter.convert(type, map);
	}
}
