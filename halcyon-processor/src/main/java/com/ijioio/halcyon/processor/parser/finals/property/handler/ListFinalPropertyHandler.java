package com.ijioio.halcyon.processor.parser.finals.property.handler;

import java.util.ArrayList;
import java.util.List;

import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.ijioio.halcyon.processor.api.parser.FinalPropertyHandler;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;

public class ListFinalPropertyHandler implements FinalPropertyHandler {

	private static final ClassName propertyType = ClassName.get(List.class);

	@Override
	public ClassName getPropertyType() {
		return propertyType;
	}

	@Override
	public CodeBlock initializer(String name, TypeMeta type) {
		return CodeBlock.of(ProcessorUtil.isGeneric(type.getType()) ? "new $T<>()" : "new $T()", ArrayList.class);
	}

	@Override
	public CodeBlock merger(String name, TypeMeta type) {
		return CodeBlock.builder().addStatement("$L.clear()", name)
				.addStatement("$L.addAll(($T) value)", name, type.getType()).build();
	}
}