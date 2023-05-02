package io.github.bumblesoftware.fastload.api.external.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.SupportedVersions;

public interface MethodAbstractionApi extends SupportedVersions {
    /**
     * This is for entrypoints where the compiler cannot infer the generic value for the event context.
     * Be sure to just check whether the type is actually consistent to avoid CCE's.
     * @param <A> Any child of this class
     * @return A generic child.
     */
    @SuppressWarnings("unchecked")
    default <A extends MethodAbstractionApi> A generify() {
        return (A)this;
    }
}
