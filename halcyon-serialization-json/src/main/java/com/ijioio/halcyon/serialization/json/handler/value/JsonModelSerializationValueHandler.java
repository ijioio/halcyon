package com.ijioio.halcyon.serialization.json.handler.value;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ijioio.halcyon.core.Model;
import com.ijioio.halcyon.core.property.Property;
import com.ijioio.halcyon.serialization.exception.SerializationException;
import com.ijioio.halcyon.serialization.json.JsonSerializationContext;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;

public class JsonModelSerializationValueHandler implements JsonSerializationValueHandler<Model> {

	@Override
	public Class<Model> getType() {
		return Model.class;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void write(Model value, JsonGenerator generator, JsonSerializationContext context) throws Exception {

		generator.writeStartObject();

		if (context.getModels().containsKey(value.getId())) {

			generator.writeName(Attributes.LINK);
			context.getValueHandler(UUID.class).write(value.getId(), generator, context);

		} else {

			context.getModels().put(value.getId(), value);

			generator.writeName(Attributes.TYPE);
			context.getValueHandler(Class.class).write(value.getClass(), generator, context);

			for (Property<?> property : value.getProperties()) {

				Object propertyValue = value.getValue(property);

				if (propertyValue != null) {

					generator.writeName(property.getName());
					((JsonSerializationValueHandler<Object>) context.getValueHandler(property.getType().getRawType()))
							.write(propertyValue, generator, context);
				}
			}
		}

		generator.writeEndObject();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Model read(JsonParser parser, JsonSerializationContext context) throws Exception {

		if (parser.currentToken() != JsonToken.START_OBJECT) {
			throw new SerializationException("corrupted data: model must be an object");
		}

		parser.nextToken();

		if (parser.currentName().equals(Attributes.LINK)) {

			parser.nextToken();

			Model model = context.getModels().get(context.getValueHandler(UUID.class).read(parser, context));

			if (model == null) {
				throw new SerializationException(String.format("corrupted data: invalid %s", Attributes.LINK));
			}

			while (parser.nextToken() != JsonToken.END_OBJECT) {

				parser.nextToken();
				parser.skipChildren();
			}

			return model;
		}

		if (parser.currentName().equals(Attributes.TYPE)) {

			parser.nextToken();

			Model model = (Model) context.getValueHandler(Class.class).read(parser, context).getDeclaredConstructor()
					.newInstance();

			parser.nextToken();

			if (!parser.currentName().equals("id")) {
				throw new SerializationException("corrupted data: id must follow type");
			}

			parser.nextToken();

			model.setId(context.getValueHandler(UUID.class).read(parser, context));

			context.getModels().put(model.getId(), model);

			Map<String, Property<?>> properties = model.getProperties().stream()
					.collect(Collectors.toMap(item -> item.getName(), item -> item));

			while (parser.nextToken() != JsonToken.END_OBJECT) {

				String name = parser.currentName();

				parser.nextToken();

				Property<?> property = properties.get(name);

				if (property != null) {
					model.setValue((Property) property,
							context.getValueHandler(property.getType().getRawType()).read(parser, context));
				} else {
					parser.skipChildren();
				}
			}

			return model;
		}

		throw new SerializationException(
				String.format("corrupted data: %s or %s not found", Attributes.LINK, Attributes.TYPE));
	}
}
