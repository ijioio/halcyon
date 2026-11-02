package com.ijioio.halcyon.processor.api.meta;

import com.palantir.javapoet.TypeName;

public class TypeMeta {

	public static TypeMeta of(TypeName type, TypeName normalizedType, boolean isEnum) {
		return new TypeMeta(type, normalizedType, isEnum);
	}

	private final TypeName type;

	private final TypeName normalizedType;

	private final boolean isEnum;

	private TypeMeta(TypeName type, TypeName normalizedType, boolean isEnum) {

		this.type = type;
		this.normalizedType = normalizedType;
		this.isEnum = isEnum;
	}

	public TypeName getType() {
		return type;
	}

	public TypeName getNormalizedType() {
		return normalizedType;
	}

	public boolean isEnum() {
		return isEnum;
	}

	@Override
	public String toString() {
		return "TypeMeta [type=" + type + ", normalizedType=" + normalizedType + ", enum=" + isEnum + "]";
	}
}