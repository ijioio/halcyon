package com.ijioio.halcyon.core;

import java.util.Objects;
import java.util.UUID;

/**
 * Reference class.
 */
public class Reference<M extends RootModel> {

	public static <M extends RootModel> Reference<M> of(final UUID id, final Class<M> type) {
		return of(id, type, null);
	}

	public static <M extends RootModel> Reference<M> of(final UUID id, final Class<M> type, final String caption) {

		Reference<M> reference = new Reference<>();

		reference.setId(Objects.requireNonNull(id));
		reference.setType(Objects.requireNonNull(type));
		reference.setCaption(caption);

		return reference;
	}

	private UUID id;

	private Class<M> type;

	private String caption;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Class<M> getType() {
		return type;
	}

	public void setType(Class<M> type) {
		this.type = type;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
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

		Reference<?> other = (Reference<?>) obj;

		return Objects.equals(id, other.id) && Objects.equals(type, other.type);
	}
}
