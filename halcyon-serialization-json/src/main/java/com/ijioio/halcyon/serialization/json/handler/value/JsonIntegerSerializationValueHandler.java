package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonIntegerSerializationValueHandler implements JsonSerializationValueHandler<Integer> {

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public void write(Integer value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeNumber(value);
	}

	@Override
	public Integer read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getIntValue();
	}
}
