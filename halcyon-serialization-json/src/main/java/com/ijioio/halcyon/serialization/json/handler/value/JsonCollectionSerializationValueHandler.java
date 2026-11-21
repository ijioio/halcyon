package com.ijioio.halcyon.serialization.json.handler.value;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.ijioio.halcyon.serialization.converter.Converter;
import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;

@SuppressWarnings("rawtypes")
public class JsonCollectionSerializationValueHandler implements JsonSerializationValueHandler<Collection> {

	@Override
	public Class<Collection> getType() {
		return Collection.class;
	}

	@Override
	public void write(Collection value, JsonGenerator generator, JsonSerializationContext context) throws Exception {

		generator.writeStartObject();

		generator.writeName(Attributes.TYPE);
		context.getValueHandler(Class.class).write(value.getClass(), generator, context);

		generator.writeName("elements");
		generator.writeStartArray();

		for (Object item : value) {

			if (item != null) {
				writeObject(item, generator, context);
			} else {
				generator.writeNull();
			}
		}

		generator.writeEndArray();
		generator.writeEndObject();
	}

	@Override
	public Collection read(JsonParser parser, JsonSerializationContext context) throws Exception {

		if (parser.currentToken() != JsonToken.START_OBJECT) {
			throw new SerializationException("corrupted data: collection must be an object");
		}

		parser.nextToken();

		if (!parser.currentName().equals(Attributes.TYPE)) {
			throw new SerializationException(String.format("corrupted data: %s not found", Attributes.TYPE));
		}

		parser.nextToken();

		Class<?> type = context.getValueHandler(Class.class).read(parser, context);

		parser.nextToken();

		if (!parser.currentName().equals("elements")) {
			throw new SerializationException("corrupted data: elements not found");
		}

		parser.nextToken();

		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new SerializationException("corrupted data: elements must be an array");
		}

		Collection<Object> collection = Set.class.isAssignableFrom(type) ? new LinkedHashSet<>() : new ArrayList<>();

		while (parser.nextToken() != JsonToken.END_ARRAY) {

			if (parser.currentToken() != JsonToken.VALUE_NULL) {
				collection.add(readObject(parser, context));
			} else {
				collection.add(null);
			}
		}

		while (parser.nextToken() != JsonToken.END_OBJECT) {

			parser.nextToken();
			parser.skipChildren();
		}

		return convert(type, collection, context);
	}

	@SuppressWarnings("unchecked")
	protected Collection<Object> convert(Class<?> type, Collection<Object> collection, JsonSerializationContext context)
			throws Exception {

		if (collection.getClass().equals(type)) {
			return collection;
		}

		Constructor<?> constructor = null;

		try {
			constructor = type.getConstructor();
		} catch (NoSuchMethodException e) {
			// Ignore
		}

		if (constructor != null && constructor.canAccess(null)) {

			Collection<Object> instance = (Collection<Object>) constructor.newInstance();

			instance.addAll(collection);

			return instance;
		}

		Converter<Collection<Object>> converter = context.getConverter(type.getName());

		return converter.convert(type, collection);
	}
}
