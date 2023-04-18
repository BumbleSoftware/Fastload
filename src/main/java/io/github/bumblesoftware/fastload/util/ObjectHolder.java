package io.github.bumblesoftware.fastload.util;

/**
 * It holds objects to allow modification through method calls.
 * @param <Type> any object.
 */
public final class ObjectHolder<Type> {
    public Type heldObj;
    public ObjectHolder(Type heldObj) {
        this.heldObj = heldObj;
    }
}
