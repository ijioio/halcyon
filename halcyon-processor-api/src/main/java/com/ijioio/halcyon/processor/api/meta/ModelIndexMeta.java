package com.ijioio.halcyon.processor.api.meta;

import java.util.ArrayList;
import java.util.List;

public class ModelIndexMeta {

	public static ModelIndexMeta of(String name, String packageName, TypeMeta model,
			List<ModelIndexPropertyMeta> properties) {
		return new ModelIndexMeta(name, packageName, model, properties);
	}

	private final String name;

	private final String packageName;

	private final TypeMeta model;

	private final List<ModelIndexPropertyMeta> properties = new ArrayList<>();

	private ModelIndexMeta(String name, String packageName, TypeMeta model, List<ModelIndexPropertyMeta> properties) {

		this.name = name;
		this.packageName = packageName;
		this.model = model;

		this.properties.clear();
		this.properties.addAll(properties);
	}

	public String getName() {
		return name;
	}

	public String getPackageName() {
		return packageName;
	}

	public TypeMeta getModel() {
		return model;
	}

	public List<ModelIndexPropertyMeta> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "ModelIndexMeta [name=" + name + ", packageName=" + packageName + ", model=" + model + ", properties="
				+ properties + "]";
	}
}