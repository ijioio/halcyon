package com.ijioio.halcyon.processor.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import com.ijioio.halcyon.core.Reference;
import com.ijioio.halcyon.core.annotation.ModelIndexSpec;
import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.meta.ModelIndexMeta;
import com.ijioio.halcyon.processor.api.meta.ModelIndexPropertyMeta;
import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;

public class ModelIndexParser {

	public static ModelIndexMeta parse(TypeElement type, ProcessingEnvironment environment, ProcessingRuntime runtime,
			ParseContext context, Logger log) {

		ModelIndexSpec indexModel = type.getAnnotation(ModelIndexSpec.class);

		String name = indexModel.name();
		String packageName = indexModel.packageName();
		TypeMeta model = TypeParser.parse(ProcessorUtil.toType(() -> indexModel.model(), runtime), runtime, context,
				log);

		List<ModelIndexPropertyMeta> properties = new ArrayList<>();

		properties.add(
				ModelIndexPropertyMeta.of("id", TypeMeta.of(TypeName.get(UUID.class), TypeName.get(UUID.class), false),
						CodeBlock.of("$T.randomUUID()", UUID.class)));
		properties.add(ModelIndexPropertyMeta.of("reference",
				TypeMeta.of(ParameterizedTypeName.get(ClassName.get(Reference.class), model.getType()),
						TypeName.get(Reference.class), false),
				null));

		for (Element enclosed : type.getEnclosedElements()) {

			if (enclosed.getKind() != ElementKind.METHOD) {
				continue;
			}

			ExecutableElement method = (ExecutableElement) enclosed;

			if (method.getParameters().size() > 0) {
				continue;
			}

			properties.add(ModelIndexPropertyParser.parse(method, environment, runtime, context, log));
		}

		ModelIndexMeta modelMeta = ModelIndexMeta.of(name, packageName, model, properties);

		log.debug("parse -> " + modelMeta);

		return modelMeta;
	}
}