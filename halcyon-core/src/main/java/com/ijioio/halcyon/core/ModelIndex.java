package com.ijioio.halcyon.core;

/**
 * Interface defining model index.
 */
public interface ModelIndex<M extends RootModel> extends Model {

	public Reference<M> getReference();

	public void setReference(Reference<M> reference);
}
