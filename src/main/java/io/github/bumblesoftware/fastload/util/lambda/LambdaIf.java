package io.github.bumblesoftware.fastload.util.lambda;

public interface LambdaIf {
    static LambdaIf getNew(boolean bl) {
        return () -> bl;
    }

    boolean getCondition();

    default LambdaElse runIf(Runnable runnable) {
        if (getCondition())
            runnable.run();
        return () -> this;
    }
}
