package com.ijioio.halcyon.processor.generator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import com.ijioio.halcyon.core.Model;
import com.ijioio.halcyon.core.RootModel;
import com.ijioio.halcyon.core.TypeReference;
import com.ijioio.halcyon.core.exception.IntrospectionException;
import com.ijioio.halcyon.core.property.Property;
import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.generator.ModelGenerator;
import com.ijioio.halcyon.processor.api.meta.ModelMeta;
import com.ijioio.halcyon.processor.api.meta.ModelPropertyMeta;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeSpec.Builder;
import com.palantir.javapoet.TypeVariableName;
import com.palantir.javapoet.WildcardTypeName;

public class StandardModelGenerator implements ModelGenerator {

	private final PropertiesGenerator propertiesGenerator = new PropertiesGenerator();

	@Override
	public JavaFile generate(ModelMeta modelMeta, ProcessingRuntime runtime, Logger log) {

		log.debug("generate -> " + getClass().getName());

		String typeName = modelMeta.getName();

		Builder builder = TypeSpec.classBuilder(typeName);

		builder.addModifiers(Stream.of(Modifier.PUBLIC).toArray(size -> new Modifier[size]));

		if (modelMeta.getParent() != null) {

			builder.superclass(ClassName.get(modelMeta.getParent().getPackageName(), modelMeta.getParent().getName()));

			if (modelMeta.isRoot() && !modelMeta.isInheritedRoot()) {
				builder.addSuperinterface(RootModel.class);
			}

		} else {
			builder.addSuperinterface(modelMeta.isRoot() ? RootModel.class : Model.class);
		}

		builder.addType(propertiesGenerator.generate(modelMeta, runtime, log));

		for (ModelPropertyMeta propertyMeta : modelMeta.getProperties()) {

			String propertyName = propertyMeta.getName();
			TypeName propertyType = propertyMeta.getType().getType();

			FieldSpec.Builder fieldBuilder = FieldSpec.builder(propertyType, propertyName);

			fieldBuilder.addModifiers(Modifier.PRIVATE);

			if (propertyMeta.isFinal()) {
				fieldBuilder.addModifiers(Modifier.FINAL);
			}

			if (propertyMeta.getInitializer() != null) {
				fieldBuilder.initializer(propertyMeta.getInitializer());
			}

			builder.addField(fieldBuilder.build());
		}

		generateToCaptionMethod(modelMeta, builder, runtime, log);
		generateGetValueMethod(modelMeta, builder, runtime, log);
		generateSetValueMethod(modelMeta, builder, runtime, log);
		generateGetPropertiesMethod(modelMeta, builder, runtime, log);

		return JavaFile.builder(modelMeta.getPackageName(), builder.build()).build();
	}

	private void generateToCaptionMethod(ModelMeta modelMeta, Builder builder, ProcessingRuntime runtime, Logger log) {

		if (modelMeta.isEffectivelyRoot()) {

			ModelPropertyMeta captionProperty = modelMeta.getProperties().stream().filter(item -> item.isCaption())
					.findFirst().orElse(null);

			if (captionProperty != null || !modelMeta.isInheritedRoot()) {

				MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("toCaption");

				methodBuilder.addModifiers(Modifier.PUBLIC);
				methodBuilder.returns(String.class);

				if (captionProperty != null) {
					methodBuilder.addStatement("return $1L != null ? $2T.valueOf($1L) : null",
							captionProperty.getName(), String.class);
				} else {
					methodBuilder.addStatement("return null");
				}

				builder.addMethod(methodBuilder.build());
			}
		}
	}

	private void generateGetValueMethod(ModelMeta modelMeta, Builder builder, ProcessingRuntime runtime, Logger log) {

		ClassName propertiesType = ClassName.get(modelMeta.getPackageName(), modelMeta.getName(),
				PropertiesGenerator.typeName);

		CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

		if (modelMeta.getProperties().size() > 0) {

			int count = 0;

			for (ModelPropertyMeta propertyMeta : modelMeta.getProperties()) {

				String propertyName = propertyMeta.getName();
				TypeName propertyType = propertyMeta.getType().getType();

				if (count == 0) {
					codeBlockBuilder.beginControlFlow("if ($T.$L.equals(property))", propertiesType, propertyName);
				} else {
					codeBlockBuilder.nextControlFlow("else if ($T.$L.equals(property))", propertiesType, propertyName);
				}

				if (propertyType.isPrimitive()) {
					codeBlockBuilder.addStatement("return ($T) ($T) $L", TypeVariableName.get("T"), propertyType.box(),
							propertyName);
				} else {
					codeBlockBuilder.addStatement("return ($T) $L", TypeVariableName.get("T"), propertyName);
				}

				if (count == modelMeta.getProperties().size() - 1) {

					codeBlockBuilder.nextControlFlow("else");

					if (modelMeta.getParent() != null) {
						codeBlockBuilder.addStatement("return super.getValue(property)");
					} else {
						codeBlockBuilder.addStatement("throw new $T($T.format($S, property))",
								IntrospectionException.class, String.class, "property %s is not supported");
					}

					codeBlockBuilder.endControlFlow();
				}

				count++;
			}

		} else {

			if (modelMeta.getParent() != null) {
				codeBlockBuilder.addStatement("return super.getValue(property)");
			} else {
				codeBlockBuilder.addStatement("throw new $T($T.format($S, property))", IntrospectionException.class,
						String.class, "property %s is not supported");
			}
		}

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getValue");

		if (modelMeta.getProperties().size() > 0) {
			methodBuilder.addAnnotation(
					AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build());
		}

		methodBuilder.addAnnotation(AnnotationSpec.builder(Override.class).build());
		methodBuilder.addModifiers(Modifier.PUBLIC);
		methodBuilder.addTypeVariable(TypeVariableName.get("T"));
		methodBuilder.returns(TypeVariableName.get("T"));
		methodBuilder.addParameter(ParameterSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Property.class), TypeVariableName.get("T")),
						"property")
				.build());
		methodBuilder.addException(IntrospectionException.class);
		methodBuilder.addCode(codeBlockBuilder.build());

		builder.addMethod(methodBuilder.build());
	}

	private void generateSetValueMethod(ModelMeta modelMeta, Builder builder, ProcessingRuntime runtime, Logger log) {

		ClassName propertiesType = ClassName.get(modelMeta.getPackageName(), modelMeta.getName(),
				PropertiesGenerator.typeName);

		CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

		if (modelMeta.getProperties().size() > 0) {

			int count = 0;

			for (ModelPropertyMeta propertyMeta : modelMeta.getProperties()) {

				String propertyName = propertyMeta.getName();
				TypeName propertyType = propertyMeta.getType().getType();

				if (count == 0) {
					codeBlockBuilder.beginControlFlow("if ($T.$L.equals(property))", propertiesType, propertyName);
				} else {
					codeBlockBuilder.nextControlFlow("else if ($T.$L.equals(property))", propertiesType, propertyName);
				}

				if (propertyMeta.getMerger() != null) {
					codeBlockBuilder.add(propertyMeta.getMerger());
				} else {
					codeBlockBuilder.addStatement("$L = ($T) value", propertyName, propertyType.box());
				}

				if (count == modelMeta.getProperties().size() - 1) {

					codeBlockBuilder.nextControlFlow("else");

					if (modelMeta.getParent() != null) {
						codeBlockBuilder.addStatement("super.setValue(property, value)");
					} else {
						codeBlockBuilder.addStatement("throw new $T($T.format($S, property))",
								IntrospectionException.class, String.class, "property %s is not supported");
					}

					codeBlockBuilder.endControlFlow();
				}

				count++;
			}

		} else {

			if (modelMeta.getParent() != null) {
				codeBlockBuilder.addStatement("super.setValue(property, value)");
			} else {
				codeBlockBuilder.addStatement("throw new $T($T.format($S, property))", IntrospectionException.class,
						String.class, "property %s is not supported");
			}
		}

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("setValue");

		if (modelMeta.getProperties().stream().anyMatch(item -> ProcessorUtil.isGeneric(item.getType().getType()))) {
			methodBuilder.addAnnotation(
					AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build());
		}

		methodBuilder.addAnnotation(AnnotationSpec.builder(Override.class).build());
		methodBuilder.addModifiers(Modifier.PUBLIC);
		methodBuilder.addTypeVariable(TypeVariableName.get("T"));
		methodBuilder.addParameter(ParameterSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Property.class), TypeVariableName.get("T")),
						"property")
				.build());
		methodBuilder.addParameter(ParameterSpec.builder(TypeVariableName.get("T"), "value").build());
		methodBuilder.addException(IntrospectionException.class);
		methodBuilder.addCode(codeBlockBuilder.build());

		builder.addMethod(methodBuilder.build());
	}

	private void generateGetPropertiesMethod(ModelMeta modelMeta, Builder builder, ProcessingRuntime runtime,
			Logger log) {

		ClassName propertiesType = ClassName.get(modelMeta.getPackageName(), modelMeta.getName(),
				PropertiesGenerator.typeName);

		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getProperties");

		methodBuilder.addAnnotation(AnnotationSpec.builder(Override.class).build());
		methodBuilder.addModifiers(Modifier.PUBLIC);
		methodBuilder.returns(ParameterizedTypeName.get(ClassName.get(Collection.class),
				ParameterizedTypeName.get(ClassName.get(Property.class), WildcardTypeName.subtypeOf(Object.class))));
		methodBuilder.addStatement("return $T.values", propertiesType);

		builder.addMethod(methodBuilder.build());
	}

	private static class PropertiesGenerator {

		private static final String typeName = "Properties";

		private TypeSpec generate(ModelMeta modelMeta, ProcessingRuntime runtime, Logger log) {

			TypeSpec.Builder builder = TypeSpec.classBuilder(PropertiesGenerator.typeName);

			builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

			if (modelMeta.getParent() != null) {
				builder.superclass(ClassName.get(modelMeta.getParent().getPackageName(),
						modelMeta.getParent().getName(), PropertiesGenerator.typeName));
			}

			for (ModelPropertyMeta propertyMeta : modelMeta.getProperties()) {

				String propertyName = propertyMeta.getName();
				TypeName propertyType = propertyMeta.getType().getType();

				FieldSpec.Builder fieldBuilder = FieldSpec.builder(
						ParameterizedTypeName.get(ClassName.get(Property.class), propertyType.box()), propertyName);

				fieldBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
				fieldBuilder.initializer("$T.of($S, new $T() {})", Property.class, propertyName,
						ParameterizedTypeName.get(ClassName.get(TypeReference.class), propertyType.box()));

				builder.addField(fieldBuilder.build());
			}

			FieldSpec.Builder fieldBuilder = FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(Property.class), WildcardTypeName.subtypeOf(Object.class))),
					"values");

			fieldBuilder.addModifiers(Modifier.PROTECTED, Modifier.STATIC, Modifier.FINAL);
			fieldBuilder.initializer("new $T<>()", ArrayList.class);

			builder.addField(fieldBuilder.build());

			CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

			if (modelMeta.getParent() != null) {
				codeBlockBuilder.addStatement("values.addAll($T.values)",
						ClassName.get(modelMeta.getParent().getPackageName(), modelMeta.getParent().getName(),
								PropertiesGenerator.typeName));
			}

			for (ModelPropertyMeta propertyMeta : modelMeta.getProperties()) {
				codeBlockBuilder.addStatement("values.add($L)", propertyMeta.getName());
			}

			builder.addStaticBlock(codeBlockBuilder.build());

			return builder.build();
		}
	}
}