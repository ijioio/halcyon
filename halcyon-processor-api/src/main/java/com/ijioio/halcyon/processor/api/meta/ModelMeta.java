package com.ijioio.halcyon.processor.api.meta;

import java.util.ArrayList;
import java.util.List;

public class ModelMeta {

	public static ModelMeta of(String name, String packageName, boolean root, ModelMeta parent,
			List<ModelPropertyMeta> properties) {
		return new ModelMeta(name, packageName, root, parent, properties);
	}

	private final String name;

	private final String packageName;

	private final boolean root;

	private final ModelMeta parent;

	private final List<ModelPropertyMeta> properties = new ArrayList<>();

	private ModelMeta(String name, String packageName, boolean root, ModelMeta parent,
			List<ModelPropertyMeta> properties) {

		this.name = name;
		this.packageName = packageName;
		this.root = root;
		this.parent = parent;

		this.properties.clear();
		this.properties.addAll(properties);
	}

	public String getName() {
		return name;
	}

	public String getPackageName() {
		return packageName;
	}

	public boolean isRoot() {
		return root;
	}

	public ModelMeta getParent() {
		return parent;
	}

	public List<ModelPropertyMeta> getProperties() {
		return properties;
	}

	public boolean isInheritedRoot() {
		return parent != null && parent.isEffectivelyRoot();
	}

	public boolean isEffectivelyRoot() {
		return root || isInheritedRoot();
	}

	@Override
	public String toString() {
		return "ModelMeta [name=" + name + ", packageName=" + packageName + ", root=" + root + ", parent=" + parent
				+ ", properties=" + properties + "]";
	}
}