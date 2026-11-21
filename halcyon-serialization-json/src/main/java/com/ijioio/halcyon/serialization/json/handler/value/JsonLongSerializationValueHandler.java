package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonLongSerializationValueHandler implements JsonSerializationValueHandler<Long> {

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

	@Override
	public void write(Long value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeNumber(value);
	}

	@Override
	public Long read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getLongValue();
	}
}
