package com.ijioio.halcyon.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
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

	private TypeReference(Type type) {
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

	public static final class SimpleParameterizedType implements ParameterizedType {

		public static SimpleParameterizedType of(Class<?> rawType, Type ownerType, Type... arguments) {

			if (rawType == null) {
				throw new IllegalArgumentException("raw type must not be null");
			}

			if (arguments == null) {
				throw new IllegalArgumentException("arguments must not be null");
			}

			if (Arrays.stream(arguments).anyMatch(item -> item == null)) {
				throw new IllegalArgumentException("arguments must not contain null values");
			}

			if (rawType.getTypeParameters().length != arguments.length) {
				throw new IllegalArgumentException("raw type parameters and arguments must be of the same length");
			}

			Class<?> declaringClass = rawType.getDeclaringClass();

			if (declaringClass == null || Modifier.isStatic(rawType.getModifiers())) {

				if (ownerType != declaringClass) {
					throw new IllegalArgumentException("owner type must match the raw type declaring class");
				}

				if (arguments.length == 0) {
					throw new IllegalArgumentException("arguments must not be empty");
				}

			} else if (ownerType instanceof Class<?> ownerClass) {

				if (ownerClass != declaringClass) {
					throw new IllegalArgumentException("owner type must match the raw type declaring class");
				}

				if (arguments.length == 0) {
					throw new IllegalArgumentException("arguments must not be empty");
				}

			} else if (ownerType instanceof ParameterizedType ownerParameterized) {

				if (ownerParameterized.getRawType() != declaringClass) {
					throw new IllegalArgumentException("owner type must match the raw type declaring class");
				}

			} else {
				throw new IllegalArgumentException("owner type must be a class or a parameterized type");
			}

			return new SimpleParameterizedType(rawType, ownerType, arguments.clone());
		}

		private final Class<?> rawType;

		private final Type ownerType;

		private final Type[] arguments;

		private SimpleParameterizedType(Class<?> rawType, Type ownerType, Type[] arguments) {

			this.rawType = rawType;
			this.ownerType = ownerType;
			this.arguments = arguments;
		}

		@Override
		public Type getRawType() {
			return rawType;
		}

		@Override
		public Type getOwnerType() {
			return ownerType;
		}

		@Override
		public Type[] getActualTypeArguments() {
			return arguments.clone();
		}

		@Override
		public int hashCode() {
			return Objects.hash(rawType, ownerType, Arrays.hashCode(arguments));
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

			SimpleParameterizedType other = (SimpleParameterizedType) obj;

			return Objects.equals(rawType, other.rawType) && Objects.equals(ownerType, other.ownerType)
					&& Arrays.equals(arguments, other.arguments);
		}

		@Override
		public String toString() {
			return "SimpleParameterizedType [rawType=" + rawType + ", ownerType=" + ownerType + ", arguments="
					+ Arrays.toString(arguments) + "]";
		}
	}

	public static final class SimpleGenericArrayType implements GenericArrayType {

		public static SimpleGenericArrayType of(Type type) {

			if (type == null) {
				throw new IllegalArgumentException("type must not be null");
			}

			if (!(type instanceof ParameterizedType) && !(type instanceof TypeVariable<?>)
					&& !(type instanceof GenericArrayType)) {
				throw new IllegalArgumentException(
						"type must be a parameterized type, a type variable, or a generic array type");
			}

			return new SimpleGenericArrayType(type);
		}

		private final Type type;

		private SimpleGenericArrayType(Type type) {
			this.type = type;
		}

		@Override
		public Type getGenericComponentType() {
			return type;
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

			SimpleGenericArrayType other = (SimpleGenericArrayType) obj;

			return Objects.equals(type, other.type);
		}

		@Override
		public String toString() {
			return "SimpleGenericArrayType [type=" + type + "]";
		}
	}

	public static final class SimpleWildcardType implements WildcardType {

		public static SimpleWildcardType of(Type upperBound, Type lowerBound) {

			if (upperBound != null && lowerBound != null) {
				throw new IllegalArgumentException("only upper bound or lower bound is allowed");
			}

			if (upperBound != null) {

				if (upperBound instanceof Class<?> type && type.isPrimitive()) {
					throw new IllegalArgumentException("upper bound must not be a primitive type");
				}

				if (upperBound instanceof WildcardType) {
					throw new IllegalArgumentException("upper bound must not be a wildcard type");
				}
			}

			if (lowerBound != null) {

				if (lowerBound instanceof Class<?> type && type.isPrimitive()) {
					throw new IllegalArgumentException("lower bound must not be a primitive type");
				}

				if (lowerBound instanceof WildcardType) {
					throw new IllegalArgumentException("lower bound must not be a wildcard type");
				}
			}

			return new SimpleWildcardType(upperBound, lowerBound);
		}

		private final Type[] upperBounds;

		private final Type[] lowerBounds;

		private SimpleWildcardType(Type upperBound, Type lowerBound) {

			this.upperBounds = upperBound != null ? new Type[] { upperBound } : new Type[] { Object.class };
			this.lowerBounds = lowerBound != null ? new Type[] { lowerBound } : new Type[0];
		}

		@Override
		public Type[] getUpperBounds() {
			return upperBounds.clone();
		}

		@Override
		public Type[] getLowerBounds() {
			return lowerBounds.clone();
		}

		@Override
		public int hashCode() {
			return Objects.hash(Arrays.hashCode(upperBounds), Arrays.hashCode(lowerBounds));
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

			SimpleWildcardType other = (SimpleWildcardType) obj;

			return Arrays.equals(upperBounds, other.upperBounds) && Arrays.equals(lowerBounds, other.lowerBounds);
		}

		@Override
		public String toString() {
			return "SimpleWildcardType [upperBounds=" + Arrays.toString(upperBounds) + ", lowerBounds="
					+ Arrays.toString(lowerBounds) + "]";
		}
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
