package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.concurrent.CountDownLatch;

class MonooBlockSubscriber<T> extends CountDownLatch implements Subscriber<T> {
    private T value;
    private Throwable error;

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
        this.error = t;
        countDown();
    }

    @Override
    public void onComplete() {
        countDown();
    }

    public T blockingGet() {
        if (Thread.currentThread() instanceof NonBlockingThread) {
            throw new IllegalStateException("block() are blocking, which is not supported in thread " + Thread.currentThread().getName());
        }

        try {
            await();
        } catch (InterruptedException e) {
            onError(e);
        }

        if (this.error != null) {
            propagateError(error);
        }

        return value;
    }

    private void propagateError(Throwable t) {
        throw new RuntimeException("unhandled error", t);
    }
}
