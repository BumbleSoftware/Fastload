package io.github.bumblesoftware.fastload.util;

import java.util.List;
import java.util.function.Consumer;

@FunctionalInterface
public interface Quickerator<T> {
    List<T> getValues();

    default void forEach(final Consumer<T> provider) {
        for (T type : getValues()) {
            if (type != null)
                provider.accept(type);
        }
    }
}
