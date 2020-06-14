package com.github.kevinjom.rx.example;

import org.reactivestreams.Publisher;

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

    public <O> Monoo<O> flatMap(Function<T, Monoo<O>> mapper) {
        // in order to chain the following opeartors, it has to return a Monoo
        return new MonooFlatMap<>(this, mapper);
    }

    public <O> Monoo<O> filter(Predicate<T> predicate) {
        return new MonooFilter(this, predicate);
    }


    public T block() {
        MonooBlockSubscriber<T> subscriber = new MonooBlockSubscriber<>();
        this.subscribe(subscriber);
        return subscriber.blockingGet();
    }

}
