package io.github.bumblesoftware.fastload.util;

/**
 * It holds objects to allow modification through method calls.
 * @param <T> any object.
 */
public final class ObjectHolder<T> {
    public T heldObj;
    public ObjectHolder(T heldObj) {
        this.heldObj = heldObj;
    }
    public ObjectHolder() {
        this(null);
    }
}
