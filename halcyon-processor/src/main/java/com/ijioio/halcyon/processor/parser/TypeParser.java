package com.ijioio.halcyon.processor.parser;

import javax.lang.model.type.TypeMirror;

import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.meta.TypeMeta;
import com.ijioio.halcyon.processor.api.util.ProcessorUtil;
import com.palantir.javapoet.TypeName;

public class TypeParser {

	public static TypeMeta parse(TypeMirror mirror, ProcessingRuntime runtime, ParseContext context, Logger log) {

		ProcessorUtil.validate(mirror);

		return TypeMeta.of(TypeName.get(mirror), ProcessorUtil.getNormalizedType(mirror, runtime),
				ProcessorUtil.isEnum(mirror, runtime));
	}
}