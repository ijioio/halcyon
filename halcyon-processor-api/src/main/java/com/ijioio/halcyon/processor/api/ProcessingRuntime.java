package com.ijioio.halcyon.processor.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ProcessingRuntime {

	public static ProcessingRuntime of(Elements elements, Types types) {
		return new ProcessingRuntime(elements, types);
	}

	private final Elements elements;

	private final Types types;

	private final Map<Class<?>, List<?>> services = new HashMap<>();

	private ProcessingRuntime(Elements elements, Types types) {

		this.elements = elements;
		this.types = types;
	}

	public Elements getElements() {
		return elements;
	}

	public Types getTypes() {
		return types;
	}

	@SuppressWarnings("unchecked")
	public <S> List<S> getServices(Class<S> type) {
		return (List<S>) services.computeIfAbsent(type,
				key -> ServiceLoader.load(type, getClass().getClassLoader()).stream().map(item -> item.get()).toList());
	}
}
