package com.kinfo.pixelart.pixeleditor;

/**
 * Interface for a method of recycling objects in a list to avoid
 * constant re-allocation of objects.
 * @param <E> Type that will be recycled.
 */
public interface Recycler<E> {
    public void recycle(E target, Object... args);
}
