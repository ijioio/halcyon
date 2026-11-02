package com.ijioio.halcyon.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Objects;

public abstract class TypeReference<T> {

	private static final TypeVisitor<Class<?>> rawTypeVisitor = new TypeVisitor<Class<?>>() {

		@Override
		protected Class<?> visitTypeVariable(TypeVariable<?> type) {
			return visit(type.getBounds()[0]);
		}

		@Override
		protected Class<?> visitWildcardType(WildcardType type) {
			return visit(type.getUpperBounds()[0]);
		}

		@Override
		protected Class<?> visitParameterizedType(ParameterizedType type) {
			return visit(type.getRawType());
		}

		@Override
		protected Class<?> visitClass(Class<?> type) {
			return type;
		}

		@Override
		protected Class<?> visitGenericArrayType(GenericArrayType type) {
			return Array.newInstance(visit(type.getGenericComponentType()), 0).getClass();
		}
	};

	private static final TypeVisitor<Type[]> argumentTypeVisitor = new TypeVisitor<>() {

		@Override
		protected Type[] visitTypeVariable(TypeVariable<?> type) {
			return visit(type.getBounds()[0]);
		}

		@Override
		protected Type[] visitWildcardType(WildcardType type) {
			return visit(type.getUpperBounds()[0]);
		}

		@Override
		protected Type[] visitParameterizedType(ParameterizedType type) {
			return type.getActualTypeArguments();
		}
	};

	public static <T> TypeReference<T> of(Type type) {
		return new SimpleTypeReference<>(type);
	}

	private final Type type;

	protected TypeReference() {

		Type superType = getClass().getGenericSuperclass();

		if (!(superType instanceof ParameterizedType)) {
			throw new IllegalArgumentException("generic parameters for type reference is not indicated");
		}

		this.type = ((ParameterizedType) superType).getActualTypeArguments()[0];
	}

	protected TypeReference(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@SuppressWarnings({ "unchecked" })
	public Class<T> getRawType() {
		return (Class<T>) rawTypeVisitor.visit(type);
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> TypeReference<T> getArgumentType(int index) {

		Type[] arguments = argumentTypeVisitor.visit(type);

		if (index < 0 || index >= arguments.length) {
			throw new IllegalArgumentException(String.format("argument index %s is out of bounds for %s", index, type));
		}

		return (TypeReference<T>) of(arguments[index]);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		TypeReference<?> other = (TypeReference<?>) obj;

		return Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "TypeReference [type=" + type + "]";
	}

	private static final class SimpleTypeReference<T> extends TypeReference<T> {

		private SimpleTypeReference(Type type) {
			super(type);
		}
	}

	private static abstract class TypeVisitor<R> {

		public final R visit(Type type) {

			if (type instanceof TypeVariable<?>) {
				return visitTypeVariable((TypeVariable<?>) type);
			} else if (type instanceof WildcardType) {
				return visitWildcardType((WildcardType) type);
			} else if (type instanceof ParameterizedType) {
				return visitParameterizedType((ParameterizedType) type);
			} else if (type instanceof Class) {
				return visitClass((Class<?>) type);
			} else if (type instanceof GenericArrayType) {
				return visitGenericArrayType((GenericArrayType) type);
			} else {
				throw new AssertionError(String.format("type %s is not supported", type));
			}
		}

		protected R visitTypeVariable(TypeVariable<?> type) {
			return unsupported(type);
		}

		protected R visitWildcardType(WildcardType type) {
			return unsupported(type);
		}

		protected R visitParameterizedType(ParameterizedType type) {
			return unsupported(type);
		}

		protected R visitClass(Class<?> type) {
			return unsupported(type);
		}

		protected R visitGenericArrayType(GenericArrayType type) {
			return unsupported(type);
		}

		protected R unsupported(Type type) {
			throw new IllegalArgumentException(String.format("type %s is not supported", type));
		}
	}
}
