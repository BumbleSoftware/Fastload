package io.github.bumblesoftware.fastload.updated_code.obj_holders;

public class ImmutableObjectHolder<T> implements ObjectHolder<T> {

    private final T heldObj;

    public ImmutableObjectHolder(T heldObj) {
        this.heldObj = heldObj;
    }

    public ImmutableObjectHolder() {
        this(null);
    }

    @Override
    public T getHeldObj() {
        return heldObj;
    }

    @Override
    public ObjectHolder<T> mutateHeldObj(T newObj) {
        return new ImmutableObjectHolder<>(heldObj);
    }
}
