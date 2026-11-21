package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonShortSerializationValueHandler implements JsonSerializationValueHandler<Short> {

	@Override
	public Class<Short> getType() {
		return Short.class;
	}

	@Override
	public void write(Short value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeNumber(value);
	}

	@Override
	public Short read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getShortValue();
	}
}
