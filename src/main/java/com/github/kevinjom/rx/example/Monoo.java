package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.concurrent.CountDownLatch;
import java.util.function.*;

public abstract class Monoo<T> implements Publisher<T> {
    public static <T> Monoo<T> just(T value) {
        return new MonooJust<T>(value);
    }

    //TODO: return handle for cancellation
    public void subscribe(Consumer<T> valueConsumer, Consumer<Throwable> errorConsumer, Runnable completeConsumer) {
        this.subscribe(new LambdaMonooSubscriber<>(valueConsumer, errorConsumer, completeConsumer));
    }

    public <O> Monoo<O> map(Function<T, O> mapper) {
        // in order to chain the following opeartors, it has to return a Monoo
        return new MonooMap(this, mapper);
    }

    public T block() {
        MonooBlockSubscriber<T> subscriber = new MonooBlockSubscriber<>();
        this.subscribe(subscriber);
        return subscriber.blockingGet();
    }

    private static class MonooBlockSubscriber<T> implements Subscriber<T> {
        private T value;
        CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void onSubscribe(Subscription s) {
            s.request(Integer.MAX_VALUE);
        }

        @Override
        public void onNext(T t) {
            this.value = t;
            latch.countDown();
        }

        @Override
        public void onError(Throwable t) {
            // TODO:
        }

        @Override
        public void onComplete() {
            latch.countDown();
        }

        public T blockingGet() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                onError(e);
            }

            return value;
        }
    }
}
