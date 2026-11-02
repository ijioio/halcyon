package com.rondhuit.platform.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.ijioio.halcyon.core.annotation.ModelIndexSpec;
import com.ijioio.halcyon.core.annotation.ModelSpec;
import com.ijioio.halcyon.processor.api.Logger;
import com.ijioio.halcyon.processor.api.Logger.MessageContext;
import com.ijioio.halcyon.processor.api.ParseContext;
import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.exception.DeferredElementException;
import com.ijioio.halcyon.processor.api.exception.GenerateException;
import com.ijioio.halcyon.processor.api.exception.ProcessorException;
import com.ijioio.halcyon.processor.api.generator.ModelGenerator;
import com.ijioio.halcyon.processor.api.generator.ModelIndexGenerator;
import com.ijioio.halcyon.processor.api.meta.ModelIndexMeta;
import com.ijioio.halcyon.processor.api.meta.ModelMeta;
import com.ijioio.halcyon.processor.parser.ModelIndexParser;
import com.ijioio.halcyon.processor.parser.ModelParser;

public class Processor extends AbstractProcessor {

	private ProcessingEnvironment environment;

	private ProcessingRuntime runtime;

	private Logger log;

	private final List<String> deferredModels = new ArrayList<>();

	private final List<String> deferredModelIndexes = new ArrayList<>();

	private int round = 1;

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(ModelSpec.class.getName(), ModelIndexSpec.class.getName());
	}

	@Override
	public synchronized void init(ProcessingEnvironment environment) {

		super.init(environment);

		boolean verbose = Boolean.parseBoolean(environment.getOptions().get("verbose"));

		this.environment = environment;
		this.runtime = ProcessingRuntime.of(environment.getElementUtils(), environment.getTypeUtils());
		this.log = Logger.of(environment.getMessager(), verbose);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		try {

			log.debug("round -> " + round);

			for (String element : List.copyOf(deferredModels)) {

				TypeElement typeElement = environment.getElementUtils().getTypeElement(element);

				log.debug("defer -> check -> " + typeElement);

				if (processModel(environment.getElementUtils().getTypeElement(element))) {

					log.debug("defer -> remove -> " + typeElement);

					deferredModels.remove(element);
				}
			}

			for (Element element : roundEnv.getElementsAnnotatedWith(ModelSpec.class)) {

				TypeElement typeElement = (TypeElement) element;

				if (!processModel(typeElement)) {

					log.debug("defer -> add -> " + typeElement);

					deferredModels.add(typeElement.getQualifiedName().toString());
				}
			}

			for (String element : List.copyOf(deferredModelIndexes)) {

				TypeElement typeElement = environment.getElementUtils().getTypeElement(element);

				log.debug("defer -> check -> " + typeElement);

				if (processModelIndex(environment.getElementUtils().getTypeElement(element))) {

					log.debug("defer -> remove -> " + typeElement);

					deferredModelIndexes.remove(element);
				}
			}

			for (Element element : roundEnv.getElementsAnnotatedWith(ModelIndexSpec.class)) {

				TypeElement typeElement = (TypeElement) element;

				if (!processModelIndex(typeElement)) {

					log.debug("defer -> add -> " + typeElement);

					deferredModelIndexes.add(typeElement.getQualifiedName().toString());
				}
			}

			if (roundEnv.processingOver()) {

				for (String deferredModel : deferredModels) {
					log.error("Unable to resolve model dependencies",
							MessageContext.of(environment.getElementUtils().getTypeElement(deferredModel)));
				}

				for (String deferredModelIndex : deferredModelIndexes) {
					log.error("Unable to resolve model index dependencies",
							MessageContext.of(environment.getElementUtils().getTypeElement(deferredModelIndex)));
				}
			}

			round++;

		} catch (ProcessorException e) {
			log.error(e.getMessage(), e.getContext());
		}

		return true;
	}

	private boolean processModel(TypeElement element) {

		try {

			ModelMeta modelMeta = ModelParser.parse(element, environment, runtime, ParseContext.of(element), log);

			for (ModelGenerator modelGenerator : runtime.getServices(ModelGenerator.class)) {
				modelGenerator.generate(modelMeta, runtime, log).writeTo(environment.getFiler());
			}

			return true;

		} catch (DeferredElementException e) {
			return false;
		} catch (IOException e) {
			throw new GenerateException("Unable to write file", MessageContext.of(element), e);
		}
	}

	private boolean processModelIndex(TypeElement element) {

		try {

			ModelIndexMeta modelIndexMeta = ModelIndexParser.parse(element, environment, runtime,
					ParseContext.of(element), log);

			for (ModelIndexGenerator modelIndexGenerator : runtime.getServices(ModelIndexGenerator.class)) {
				modelIndexGenerator.generate(modelIndexMeta, runtime, log).writeTo(environment.getFiler());
			}

			return true;

		} catch (DeferredElementException e) {
			return false;
		} catch (IOException e) {
			throw new GenerateException("Unable to write file", MessageContext.of(element), e);
		}
	}
}