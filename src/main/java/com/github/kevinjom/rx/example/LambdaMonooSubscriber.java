package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.function.Consumer;

class LambdaMonooSubscriber<T> implements Subscriber<T> {
    private final Consumer<T> valueConsumer;
    private final Consumer<Throwable> errorConsumer;
    private final Runnable completeConsumer;

    public LambdaMonooSubscriber(Consumer<T> valueConsumer, Consumer<Throwable> errorConsumer, Runnable completeConsumer) {
        this.valueConsumer = valueConsumer;
        this.errorConsumer = errorConsumer;
        this.completeConsumer = completeConsumer;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        if (valueConsumer != null) {
            try {
                valueConsumer.accept(t);
            } catch (Throwable e) {
                propogateError(e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        if (errorConsumer != null) {
            errorConsumer.accept(t);
        } else {
            propogateError(t);
        }
    }

    private void propogateError(Throwable t) {
        throw new RuntimeException("unhandled error", t);
    }

    @Override
    public void onComplete() {
        if (completeConsumer != null) {
            try {
                completeConsumer.run();
            } catch (Throwable e) {
                propogateError(e);
            }
        }
    }
}
