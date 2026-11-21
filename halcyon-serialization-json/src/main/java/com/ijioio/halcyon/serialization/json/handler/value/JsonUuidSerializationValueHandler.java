package com.ijioio.halcyon.serialization.json.handler.value;

import java.util.UUID;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonUuidSerializationValueHandler implements JsonSerializationValueHandler<UUID> {

	@Override
	public Class<UUID> getType() {
		return UUID.class;
	}

	@Override
	public void write(UUID value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeString(value.toString());
	}

	@Override
	public UUID read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return UUID.fromString(parser.getValueAsString());
	}
}
