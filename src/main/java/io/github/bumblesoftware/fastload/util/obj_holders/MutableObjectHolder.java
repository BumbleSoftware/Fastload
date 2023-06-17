package io.github.bumblesoftware.fastload.util.obj_holders;

import java.util.function.Function;

/**
 * It holds objects to allow modification through method calls.
 * @param <T> any object.
 */
public class MutableObjectHolder<T> extends ImmutableObjectHolder<T> {
    public MutableObjectHolder(T heldObj) {
        super(heldObj);
    }

    public MutableObjectHolder() {
        super();
    }

    public void setHeldObj(T heldObj) {
        super.heldObj = heldObj;
    }

    @SuppressWarnings("unused")
    public void modifyHeldObj(final Function<T, T> withHeldObj) {
        super.heldObj = withHeldObj.apply(super.heldObj);
    }
}
