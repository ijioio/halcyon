package com.ijioio.halcyon.processor.api.generator;

import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.meta.ModelIndexMeta;
import com.palantir.javapoet.JavaFile;

public interface ModelIndexGenerator {

	public JavaFile generate(ModelIndexMeta modelMeta, ProcessingRuntime runtime, Logger log);
}
