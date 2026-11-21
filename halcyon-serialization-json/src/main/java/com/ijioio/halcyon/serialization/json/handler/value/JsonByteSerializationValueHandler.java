package com.ijioio.halcyon.serialization.json.handler.value;

import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;

public class JsonByteSerializationValueHandler implements JsonSerializationValueHandler<Byte> {

	@Override
	public Class<Byte> getType() {
		return Byte.class;
	}

	@Override
	public void write(Byte value, JsonGenerator generator, JsonSerializationContext context) throws Exception {
		generator.writeNumber(value);
	}

	@Override
	public Byte read(JsonParser parser, JsonSerializationContext context) throws Exception {
		return parser.getByteValue();
	}
}
