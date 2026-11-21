package com.ijioio.halcyon.serialization.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ijioio.halcyon.core.Model;
import com.ijioio.halcyon.serialization.converter.Converter;
import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.handler.value.JsonSerializationValueHandler;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonSerializationContext {

	private static final Map<String, Converter<?>> converters = ServiceLoader
			.load(Converter.class, Converter.class.getClassLoader()).stream().map(item -> item.get())
			.collect(Collectors.toMap(item -> item.getType(), item -> item));

	private static final List<JsonSerializationValueHandler<?>> valueHandlers = ServiceLoader
			.load(JsonSerializationValueHandler.class, JsonSerializationValueHandler.class.getClassLoader()).stream()
			.<JsonSerializationValueHandler<?>>map(item -> item.get()).toList();

	private static final Map<Class<?>, JsonSerializationValueHandler<?>> valueHandlersCache = new ConcurrentHashMap<>();

	private static final JsonSerializationValueHandler<?> nullValueHandler = new JsonSerializationValueHandler<>() {

		public Class<Object> getType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void write(Object value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object read(JsonParser parser, JsonSerializationContext context) throws Exception {
			throw new UnsupportedOperationException();
		}
	};

	public static JsonSerializationContext of() {
		return new JsonSerializationContext();
	}

	private final Map<UUID, Model> models = new HashMap<>();

	private <T> JsonSerializationContext() {
	}

	@SuppressWarnings("unchecked")
	public <T> Converter<T> getConverter(String type) throws SerializationException {

		Converter<?> converter = converters.get(type);

		if (converter == null) {
			throw new SerializationException(String.format("type %s is not supported", type));
		}

		return (Converter<T>) converter;
	}

	@SuppressWarnings("unchecked")
	public <T> JsonSerializationValueHandler<T> getValueHandler(Class<T> type) throws SerializationException {

		JsonSerializationValueHandler<?> valueHandler = valueHandlersCache.computeIfAbsent(type, key -> {

			List<JsonSerializationValueHandler<?>> candidateValueHandlers = valueHandlers.stream()
					.filter(item -> item.getType().isAssignableFrom(type)).toList();

			JsonSerializationValueHandler<?> selectedValueHandler = null;

			for (JsonSerializationValueHandler<?> candidateValueHandler : candidateValueHandlers) {

				if (selectedValueHandler == null
						|| selectedValueHandler.getType().isAssignableFrom(candidateValueHandler.getType())) {
					selectedValueHandler = candidateValueHandler;
				}
			}

			return selectedValueHandler != null ? selectedValueHandler : nullValueHandler;
		});

		if (valueHandler == nullValueHandler) {
			throw new SerializationException(String.format("type %s is not supported", type));
		}

		return (JsonSerializationValueHandler<T>) valueHandler;
	}

	public Map<UUID, Model> getModels() {
		return models;
	}
}
