package com.ijioio.halcyon.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ijioio.halcyon.core.RootModel;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ModelIndexSpec {

	String name();

	String packageName() default "";

	public Class<? extends RootModel> model();
}
