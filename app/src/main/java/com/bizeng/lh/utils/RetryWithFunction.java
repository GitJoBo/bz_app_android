package com.bizeng.lh.utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

public class RetryWithFunction implements
        Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private int retryCount;

    public RetryWithFunction(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> attempts) throws Exception {
        return attempts
                .flatMap((Throwable throwable) -> {
                    if (++retryCount <= maxRetries) {
                        // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                        System.out.println("get error, retry count " + retryCount);
                        return Observable.just("" + retryCount);
                    }
                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }


}