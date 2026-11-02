package com.ijioio.halcyon.processor.api.meta;

import com.palantir.javapoet.CodeBlock;

public class ModelIndexPropertyMeta {

	public static ModelIndexPropertyMeta of(String name, TypeMeta type, CodeBlock initializer) {
		return new ModelIndexPropertyMeta(name, type, initializer);
	}

	private final String name;

	private final TypeMeta type;

	private final CodeBlock initializer;

	private ModelIndexPropertyMeta(String name, TypeMeta type, CodeBlock initializer) {

		this.name = name;
		this.type = type;
		this.initializer = initializer;
	}

	public String getName() {
		return name;
	}

	public TypeMeta getType() {
		return type;
	}

	public CodeBlock getInitializer() {
		return initializer;
	}

	@Override
	public String toString() {
		return "ModelIndexPropertyMeta [name=" + name + ", type=" + type + ", initializer=" + initializer + "]";
	}
}