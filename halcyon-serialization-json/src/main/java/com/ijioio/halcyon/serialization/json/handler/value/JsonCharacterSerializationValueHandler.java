package com.ijioio.halcyon.serialization.json.handler.value;

import java.util.Optional;

import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonCharacterSerializationValueHandler implements JsonSerializationValueHandler<Character> {

	@Override
	public Class<Character> getType() {
		return Character.class;
	}

	@Override
	public void write(Character value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeString(String.valueOf(value));
	}

	@Override
	public Character read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return Optional.of(parser.getValueAsString()).filter(item -> item.length() == 1).map(item -> item.charAt(0))
				.orElseThrow(() -> new SerializationException("corrupted data: value must be a single character"));
	}
}
