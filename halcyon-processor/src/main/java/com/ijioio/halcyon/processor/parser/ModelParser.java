package com.ijioio.halcyon.processor.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.ijioio.halcyon.core.annotation.ModelSpec;
import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.Logger.MessageContext;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.exception.ParseException;
import com.ijioio.halcyon.processor.api.meta.ModelMeta;
import com.ijioio.halcyon.processor.api.meta.ModelPropertyMeta;
import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

public class ModelParser {

	public static ModelMeta parse(TypeElement type, ProcessingEnvironment environment, ProcessingRuntime runtime,
			ParseContext context, Logger log) {

		ModelSpec model = type.getAnnotation(ModelSpec.class);

		String name = model.name();
		String packageName = model.packageName();
		boolean root = model.root();

		ModelMeta parent = null;

		for (TypeMirror interfaceTypeMirror : type.getInterfaces()) {

			if (interfaceTypeMirror.getKind() != TypeKind.DECLARED) {
				throw new ParseException("Cannot resolve parent type", MessageContext.of(type));
			}

			TypeElement interfaceElement = (TypeElement) runtime.getTypes().asElement(interfaceTypeMirror);

			if (interfaceElement.getAnnotation(ModelSpec.class) != null) {

				if (parent != null) {
					throw new ParseException("Cannot extend more than one model", MessageContext.of(type));
				}

				parent = parse(interfaceElement, environment, runtime, context, log);
			}
		}

		List<ModelPropertyMeta> properties = new ArrayList<>();

		if (parent == null) {
			properties.add(
					ModelPropertyMeta.of("id", TypeMeta.of(TypeName.get(UUID.class), TypeName.get(UUID.class), false),
							CodeBlock.of("$T.randomUUID()", UUID.class), null, false, false));
		}

		for (Element enclosed : type.getEnclosedElements()) {

			if (enclosed.getKind() != ElementKind.METHOD) {
				continue;
			}

			ExecutableElement method = (ExecutableElement) enclosed;

			if (method.getParameters().size() > 0) {
				continue;
			}

			properties.add(ModelPropertyParser.parse(method, environment, runtime, context, log));
		}

		ModelMeta modelMeta = ModelMeta.of(name, packageName, root, parent, properties);

		log.debug("parse -> " + modelMeta);

		return modelMeta;
	}
}