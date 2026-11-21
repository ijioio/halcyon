package com.ijioio.halcyon.serialization.converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import com.ijioio.halcyon.serialization.exception.SerializationException;

public final class MapConverters {

	public static final class ImmutableCollectionsMap1Converter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$Map1";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {

			if (map.size() == 1) {
				return Optional.of(map.entrySet().iterator().next()).map(item -> Map.of(item.getKey(), item.getValue()))
						.get();
			}

			throw new SerializationException(
					String.format("unable to instantiate map %s with %s entries", type.getName(), map.size()));
		}
	}

	public static final class ImmutableCollectionsMapNConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.ImmutableCollections$MapN";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {

			if (map.isEmpty()) {
				return Map.of();
			}

			if (map.size() >= 2) {
				return Map.ofEntries(map.entrySet().toArray(new Entry<?, ?>[0]));
			}

			throw new SerializationException(
					String.format("unable to instantiate map %s with %s entries", type.getName(), map.size()));
		}
	}

	public static final class CollectionsEmptyMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$EmptyMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {

			if (map.isEmpty()) {
				return Collections.emptyMap();
			}

			throw new SerializationException(
					String.format("unable to instantiate map %s with %s entries", type.getName(), map.size()));
		}
	}

	public static final class CollectionsEmptyNavigableMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableNavigableMap$EmptyNavigableMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {

			if (map.isEmpty()) {
				return Collections.emptyNavigableMap();
			}

			throw new SerializationException(
					String.format("unable to instantiate map %s with %s entries", type.getName(), map.size()));
		}
	}

	public static final class CollectionsSingletonMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SingletonMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {

			if (map.size() == 1) {
				return Optional.of(map.entrySet().iterator().next())
						.map(item -> Collections.singletonMap(item.getKey(), item.getValue())).get();
			}

			throw new SerializationException(
					String.format("unable to instantiate map %s with %s entries", type.getName(), map.size()));
		}
	}

	public static final class CollectionsUnmodifiableMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.unmodifiableMap(new LinkedHashMap<>(map));
		}
	}

	public static final class CollectionsUnmodifiableNavigableMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableNavigableMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.unmodifiableNavigableMap(new TreeMap<>(map));
		}
	}

	public static final class CollectionsUnmodifiableSortedMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$UnmodifiableSortedMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.unmodifiableSortedMap(new TreeMap<>(map));
		}
	}

	public static final class CollectionsSynchronizedMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.synchronizedMap(new LinkedHashMap<>(map));
		}
	}

	public static final class CollectionsSynchronizedNavigableMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedNavigableMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.synchronizedNavigableMap(new TreeMap<>(map));
		}
	}

	public static final class CollectionsSynchronizedSortedMapConverter implements Converter<Map<Object, Object>> {

		@Override
		public String getType() {
			return "java.util.Collections$SynchronizedSortedMap";
		}

		@Override
		public Map<Object, Object> convert(Class<?> type, Map<Object, Object> map) throws Exception {
			return Collections.synchronizedSortedMap(new TreeMap<>(map));
		}
	}
}
