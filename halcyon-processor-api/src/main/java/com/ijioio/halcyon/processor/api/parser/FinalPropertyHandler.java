package com.ijioio.halcyon.processor.api.parser;

import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;

public interface FinalPropertyHandler {

	public ClassName getPropertyType();

	public CodeBlock initializer(String name, TypeMeta type);

	public CodeBlock merger(String name, TypeMeta type);
}
