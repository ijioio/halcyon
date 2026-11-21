package com.ijioio.halcyon.processor.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import com.ijioio.halcyon.core.annotation.Caption;
import com.ijioio.halcyon.core.annotation.Final;
import com.ijioio.halcyon.core.annotation.Initializer;
import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.Logger.MessageContext;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.exception.ParseException;
import com.ijioio.halcyon.processor.api.meta.ModelPropertyMeta;
import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.ijioio.halcyon.processor.api.parser.FinalPropertyHandler;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.CodeBlock;

public class ModelPropertyParser {

	public static ModelPropertyMeta parse(ExecutableElement method, ProcessingEnvironment environment,
			ProcessingRuntime runtime, ParseContext context, Logger log) {

		Final finalAnnotation = method.getAnnotation(Final.class);
		Initializer initializerAnnotation = method.getAnnotation(Initializer.class);
		Caption captionAnnotation = method.getAnnotation(Caption.class);

		String name = method.getSimpleName().toString();
		TypeMeta type = TypeParser.parse(method.getReturnType(), runtime, context, log);

		CodeBlock initializer = null;
		CodeBlock merger = null;

		if (finalAnnotation != null) {

			FinalPropertyHandler handler = runtime.getServices(FinalPropertyHandler.class).stream()
					.filter(item -> item.getPropertyType().equals(type.getNormalizedType())).findFirst().orElse(null);

			if (handler == null) {
				throw new ParseException(
						String.format("Final is not supported for property of type %s", type.getNormalizedType()),
						MessageContext.of(method, ProcessorUtil.getAnnotationMirror(method, Final.class, runtime)));
			}

			initializer = handler.initializer(name, type);
			merger = handler.merger(name, type);
		}

		if (initializerAnnotation != null) {
			initializer = CodeBlock.of("$L", initializerAnnotation.value());
		}

		ModelPropertyMeta propertyMeta = ModelPropertyMeta.of(name, type, initializer, merger, finalAnnotation != null,
				captionAnnotation != null);

		log.debug("parse -> " + propertyMeta);

		return propertyMeta;
	}
}