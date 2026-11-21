package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

@SuppressWarnings("rawtypes")
public class JsonClassSerializationValueHandler implements JsonSerializationValueHandler<Class> {

	@Override
	public Class<Class> getType() {
		return Class.class;
	}

	@Override
	public void write(Class value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeString(value.getName());
	}

	@Override
	public Class read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return Class.forName(parser.getValueAsString());
	}
}
