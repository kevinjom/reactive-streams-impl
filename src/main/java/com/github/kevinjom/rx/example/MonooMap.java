package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.function.Function;

public class MonooMap<I, O> extends Monoo<O> {
    private final Monoo<I> source;
    private final Function<I, O> mapper;

    public MonooMap(Monoo<I> source, Function<I, O> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    /**
     * Monoo.just(1).map(i -> i * 2).map(i -> "hi" + i)
     * <p>
     * When you subscribe on this publisher, we should trigger subscription to upstream publishers
     */
    @Override
    public void subscribe(Subscriber<? super O> s) {
        source.subscribe(new MapSubscriber<>(s, mapper));
    }

    private static class MapSubscriber<I, O> implements Subscriber<I> {
        private final Subscriber<? super O> actual;
        private final Function<I, O> mapper;

        public MapSubscriber(Subscriber<? super O> actual, Function<I, O> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            actual.onSubscribe(subscription); // create your own Subscription to apply logic when actual subscriber request/cancel
        }

        @Override
        public void onNext(I i) {
            try {
                O mapped = mapper.apply(i);
                actual.onNext(mapped);
            } catch (Throwable throwable) {
                onError(throwable);
            }
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }
    }
}
