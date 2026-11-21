package com.ijioio.halcyon.processor.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.meta.ModelIndexPropertyMeta;
import com.ijioio.halcyon.processor.api.meta.TypeMeta;

public class ModelIndexPropertyParser {

	public static ModelIndexPropertyMeta parse(ExecutableElement method, ProcessingEnvironment environment,
			ProcessingRuntime runtime, ParseContext context, Logger log) {

		String name = method.getSimpleName().toString();
		TypeMeta type = TypeParser.parse(method.getReturnType(), runtime, context, log);

		ModelIndexPropertyMeta propertyMeta = ModelIndexPropertyMeta.of(name, type, null);

		log.debug("parse -> " + propertyMeta);

		return propertyMeta;
	}
}