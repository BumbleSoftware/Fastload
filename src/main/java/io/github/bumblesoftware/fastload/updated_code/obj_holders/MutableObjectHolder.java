package io.github.bumblesoftware.fastload.updated_code.obj_holders;

/**
 * It holds objects to allow modification through method calls.
 * @param <T> any object.
 */
public class MutableObjectHolder<T> implements ObjectHolder<T> {
    private T heldObj;
    public MutableObjectHolder(T heldObj) {
        this.heldObj = heldObj;
    }

    @Override
    public T getHeldObj() {
        return heldObj;
    }

    @Override
    public ObjectHolder<T> mutateHeldObj(T newObj) {
        heldObj = newObj;
        return this;
    }
}
