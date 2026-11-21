package com.ijioio.halcyon.core;

import java.util.UUID;

/**
 * Interface defining identity. Identity guarantees that implementations would
 * expose an {@code id} property.
 */
public interface Identity {

	public UUID getId();

	public void setId(UUID id);
}
