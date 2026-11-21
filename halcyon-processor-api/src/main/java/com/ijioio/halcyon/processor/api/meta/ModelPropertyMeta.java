package com.ijioio.halcyon.processor.api.meta;

import com.palantir.javapoet.CodeBlock;

public class ModelPropertyMeta {

	public static ModelPropertyMeta of(String name, TypeMeta type, CodeBlock initializer, CodeBlock merger,
			boolean isFinal, boolean caption) {
		return new ModelPropertyMeta(name, type, initializer, merger, isFinal, caption);
	}

	private final String name;

	private final TypeMeta type;

	private final CodeBlock initializer;

	private final CodeBlock merger;

	private final boolean isFinal;

	private final boolean caption;

	private ModelPropertyMeta(String name, TypeMeta type, CodeBlock initializer, CodeBlock merger, boolean isFinal,
			boolean caption) {

		this.name = name;
		this.type = type;
		this.initializer = initializer;
		this.merger = merger;
		this.caption = caption;
		this.isFinal = isFinal;
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

	public CodeBlock getMerger() {
		return merger;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public boolean isCaption() {
		return caption;
	}

	@Override
	public String toString() {
		return "ModelPropertyMeta [name=" + name + ", type=" + type + ", initializer=" + initializer + ", merger="
				+ merger + ", final=" + isFinal + ", caption=" + caption + "]";
	}
}