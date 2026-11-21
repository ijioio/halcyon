package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonStringSerializationValueHandler implements JsonSerializationValueHandler<String> {

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public void write(String value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeString(value);
	}

	@Override
	public String read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getValueAsString();
	}
}
