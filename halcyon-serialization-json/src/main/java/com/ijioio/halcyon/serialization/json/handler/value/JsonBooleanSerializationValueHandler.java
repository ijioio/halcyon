package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonBooleanSerializationValueHandler implements JsonSerializationValueHandler<Boolean> {

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

	@Override
	public void write(Boolean value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeBoolean(value);
	}

	@Override
	public Boolean read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getBooleanValue();
	}
}
