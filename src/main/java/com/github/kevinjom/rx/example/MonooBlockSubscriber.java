package com.github.kevinjom.rx.example;

import org.reactivestreams.*;
import org.slf4j.*;

import java.util.concurrent.CountDownLatch;

class MonooBlockSubscriber<T> extends CountDownLatch implements Subscriber<T> {
    private static final Logger logger = LoggerFactory.getLogger(CountDownLatch.class);

    private T value;

    public MonooBlockSubscriber() {
        super(1);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        this.value = t;
        countDown();
    }

    @Override
    public void onError(Throwable t) {
        // TODO:
    }

    @Override
    public void onComplete() {
        countDown();
    }

    public T blockingGet() {
        try {
            await();
        } catch (InterruptedException e) {
            onError(e);
        }

        return value;
    }
}
