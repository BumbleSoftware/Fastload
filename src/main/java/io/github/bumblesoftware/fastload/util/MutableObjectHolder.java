package io.github.bumblesoftware.fastload.util;

import java.util.function.Function;

/**
 * It holds objects to allow modification through method calls.
 * @param <T> any object.
 */
public final class MutableObjectHolder<T> {
    public T heldObj;
    public MutableObjectHolder(T heldObj) {
        this.heldObj = heldObj;
    }
    public MutableObjectHolder() {
        this(null);
    }

    @SuppressWarnings("unused")
    public void modify(final Function<T, T> withHeldObj) {
        heldObj = withHeldObj.apply(heldObj);
    }
}
