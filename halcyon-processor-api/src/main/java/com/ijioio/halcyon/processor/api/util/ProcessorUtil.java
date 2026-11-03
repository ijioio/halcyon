package com.ijioio.halcyon.processor.api.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import com.ijioio.halcyon.processor.api.ProcessingRuntime;
import com.ijioio.halcyon.processor.api.exception.DeferredElementException;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;

public class ProcessorUtil {

	public static TypeMirror validate(TypeMirror type) {

		if (type.getKind() == TypeKind.ERROR) {
			throw new DeferredElementException(type);
		}

		if (type instanceof DeclaredType) {

			for (TypeMirror argument : ((DeclaredType) type).getTypeArguments()) {
				validate(argument);
			}
		}

		return type;
	}

	public static TypeMirror toDeclaredType(Class<?> type, TypeMirror[] arguments, ProcessingRuntime runtime) {
		return runtime.getTypes().getDeclaredType(runtime.getElements().getTypeElement(type.getCanonicalName()),
				arguments);
	}

	public static TypeMirror toType(Class<?> type, ProcessingRuntime runtime) {
		return runtime.getElements().getTypeElement(type.getCanonicalName()).asType();
	}

	public static TypeMirror toType(Supplier<Class<?>> type, ProcessingRuntime runtime) {

		try {
			return toType(type.get(), runtime);
		} catch (MirroredTypeException e) {
			return e.getTypeMirror();
		}
	}

	public static TypeMirror getRawType(TypeMirror type, ProcessingRuntime runtime) {

		if (type instanceof DeclaredType) {
			return runtime.getTypes().erasure(type);
		} else if (type instanceof ArrayType) {
			return runtime.getTypes().getArrayType(getRawType(((ArrayType) type).getComponentType(), runtime));
		} else {
			return type;
		}
	}

	public static List<TypeMirror> getParameterTypes(TypeMirror type, ProcessingEnvironment environment) {

		if (type instanceof DeclaredType) {
			return ((DeclaredType) type).getTypeArguments().stream().map(item -> (TypeMirror) item).toList();
		} else {
			return List.of();
		}
	}

	public static TypeMirror getRawEnumType(TypeMirror type) {
		if (type.getKind() != TypeKind.DECLARED) {
			return null;
		}

		DeclaredType declaredType = (DeclaredType) type;

		if (declaredType.asElement().getKind() != ElementKind.ENUM) {
			return null;
		}

		TypeMirror superclass = ((TypeElement) declaredType.asElement()).getSuperclass();

		DeclaredType enumType = (DeclaredType) superclass;

		return ((TypeElement) enumType.asElement()).asType();
	}

	public static TypeName getNormalizedType(TypeMirror mirror, ProcessingRuntime runtime) {

		if (mirror.getKind() == TypeKind.DECLARED) {

			TypeElement element = (TypeElement) ((DeclaredType) mirror).asElement();

			if (element.getKind() == ElementKind.ENUM) {
				return ClassName.get(Enum.class);
			} else {
				return ClassName.get(element);
			}

		} else if (mirror.getKind() == TypeKind.ARRAY) {

			return ArrayTypeName.of(getNormalizedType(((ArrayType) mirror).getComponentType(), runtime));

		} else if (mirror.getKind() == TypeKind.TYPEVAR) {

			return getNormalizedType(((TypeVariable) mirror).getUpperBound(), runtime);

		} else if (mirror.getKind() == TypeKind.WILDCARD) {

			TypeMirror bound = ((WildcardType) mirror).getExtendsBound();

			if (bound != null) {
				return getNormalizedType(bound, runtime);
			} else {
				return ClassName.get(Object.class);
			}

		} else {

			return TypeName.get(mirror);
		}
	}

	public static boolean isEnum(TypeMirror type, ProcessingRuntime runtime) {
		return type.getKind() == TypeKind.DECLARED && runtime.getTypes().asElement(type).getKind() == ElementKind.ENUM;
	}

	public static ClassName getRawType(TypeName type) {

		if (type instanceof ParameterizedTypeName) {
			return ((ParameterizedTypeName) type).rawType();
		} else {
			return (ClassName) type;
		}
	}

	public static List<TypeName> getParameterTypes(TypeName type) {

		if (type instanceof ParameterizedTypeName) {
			return ((ParameterizedTypeName) type).typeArguments();
		} else {
			return List.of();
		}
	}

	public static boolean isGeneric(TypeName type) {
		return getParameterTypes(type).size() > 0;
	}

	public static AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> type,
			ProcessingRuntime runtime) {

		TypeMirror typeMirror = runtime.getElements().getTypeElement(type.getCanonicalName()).asType();

		return element.getAnnotationMirrors().stream()
				.filter(mirror -> runtime.getTypes().isSameType(mirror.getAnnotationType(), typeMirror)).findFirst()
				.orElse(null);
	}

	public static String getPackageName(Element element, ProcessingRuntime runtime) {
		return runtime.getElements().getPackageOf(element).getQualifiedName().toString();
	}
}
