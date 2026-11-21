package com.ijioio.halcyon.serialization.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.ijioio.halcyon.serialization.exception.SerializationException;

public final class CollectionConverters {

	public static final class ArraysArrayListCollectionConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Arrays$ArrayList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Arrays.asList(collection.toArray());
		}
	}

	public static final class ImmutableCollectionsList12Converter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$List12";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.size() == 1) {
				return List.of(collection.iterator().next());
			}

			if (collection.size() == 2) {
				return Optional.of(collection.iterator()).map(item -> List.of(item.next(), item.next())).get();
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class ImmutableCollectionsListNConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$ListN";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.isEmpty()) {
				return List.of();
			}

			if (collection.size() >= 3) {
				return List.of(collection.toArray());
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class ImmutableCollectionsSet12Converter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$Set12";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.size() == 1) {
				return Set.of(collection.iterator().next());
			}

			if (collection.size() == 2) {
				return Optional.of(collection.iterator()).map(item -> Set.of(item.next(), item.next())).get();
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class ImmutableCollectionsSetNConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$SetN";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.isEmpty()) {
				return Set.of();
			}

			if (collection.size() >= 3) {
				return Set.of(collection.toArray());
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsEmptyListConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$EmptyList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.isEmpty()) {
				return Collections.emptyList();
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsEmptySetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$EmptySet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.isEmpty()) {
				return Collections.emptySet();
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsEmptyNavigableSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableNavigableSet$EmptyNavigableSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.isEmpty()) {
				return Collections.emptyNavigableSet();
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsSingletonListConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SingletonList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.size() == 1) {
				return Collections.singletonList(collection.iterator().next());
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsSingletonSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SingletonSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.size() == 1) {
				return Collections.singleton(collection.iterator().next());
			}

			throw new SerializationException(String.format("unable to instantiate collection %s with %s elements",
					type.getName(), collection.size()));
		}
	}

	public static final class CollectionsUnmodifiableCollectionConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableCollection";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableCollection(new ArrayList<>(collection));
		}
	}

	public static final class CollectionsUnmodifiableListConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableList(new LinkedList<>(collection));
		}
	}

	public static final class CollectionsUnmodifiableRandomAccessListConverter
			implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableRandomAccessList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableList(new ArrayList<>(collection));
		}
	}

	public static final class CollectionsUnmodifiableSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableSet(new LinkedHashSet<>(collection));
		}
	}

	public static final class CollectionsUnmodifiableNavigableSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableNavigableSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableNavigableSet(new TreeSet<>(collection));
		}
	}

	public static final class CollectionsUnmodifiableSortedSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableSortedSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.unmodifiableSortedSet(new TreeSet<>(collection));
		}
	}

	public static final class CollectionsSynchronizedCollectionConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedCollection";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedCollection(new ArrayList<>(collection));
		}
	}

	public static final class CollectionsSynchronizedListConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedList(new LinkedList<>(collection));
		}
	}

	public static final class CollectionsSynchronizedRandomAccessListConverter
			implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedRandomAccessList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedList(new ArrayList<>(collection));
		}
	}

	public static final class CollectionsSynchronizedSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedSet(new LinkedHashSet<>(collection));
		}
	}

	public static final class CollectionsSynchronizedNavigableSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedNavigableSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedNavigableSet(new TreeSet<>(collection));
		}
	}

	public static final class CollectionsSynchronizedSortedSetConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedSortedSet";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {
			return Collections.synchronizedSortedSet(new TreeSet<>(collection));
		}
	}

	public static final class CollectionsCopiesListConverter implements Converter<Collection<Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$CopiesList";
		}

		@Override
		public Collection<Object> convert(Class<?> type, Collection<Object> collection) throws Exception {

			if (collection.stream().distinct().count() <= 1) {
				return Collections.nCopies(collection.size(),
						!collection.isEmpty() ? collection.iterator().next() : null);
			}

			throw new SerializationException(
					String.format("unable to instantiate collection %s with non unique elements", type.getName()));
		}
	}
}
