package com.ijioio.halcyon.processor.api.generator;

import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.meta.ModelMeta;
import com.palantir.javapoet.JavaFile;

public interface ModelGenerator {

	public JavaFile generate(ModelMeta modelMeta, ProcessingRuntime runtime, Logger log);
}
