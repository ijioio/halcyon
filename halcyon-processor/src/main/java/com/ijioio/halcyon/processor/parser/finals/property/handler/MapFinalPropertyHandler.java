package com.ijioio.halcyon.processor.parser.finals.property.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.ijioio.halcyon.processor.api.parser.FinalPropertyHandler;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;

public class MapFinalPropertyHandler implements FinalPropertyHandler {

	private static final ClassName propertyType = ClassName.get(Map.class);

	@Override
	public ClassName getPropertyType() {
		return propertyType;
	}

	@Override
	public CodeBlock initializer(String name, TypeMeta type) {
		return CodeBlock.of(ProcessorUtil.isGeneric(type.getType()) ? "new $T<>()" : "new $T()", LinkedHashMap.class);
	}

	@Override
	public CodeBlock merger(String name, TypeMeta type) {
		return CodeBlock.builder().addStatement("$L.clear()", name)
				.addStatement("$L.putAll(($T) value)", name, type.getType()).build();
	}
}