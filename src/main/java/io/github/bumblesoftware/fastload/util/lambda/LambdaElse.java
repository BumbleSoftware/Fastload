package io.github.bumblesoftware.fastload.util.lambda;

public interface LambdaElse {
    LambdaIf getParentIf();

    default void runElse(Runnable runnable) {
        if (!getParentIf().getCondition())
            runnable.run();
    }

    default LambdaElse runElseIf(LambdaIf lambdaIf, Runnable runnable) {
        lambdaIf.runIf(() -> runElse(runnable));
        return () -> lambdaIf;
    }

    default LambdaElse runElseIf(boolean lambdaIf, Runnable runnable) {
        return runElseIf(() -> lambdaIf, runnable);
    }
}
