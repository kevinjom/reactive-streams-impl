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
        source.subscribe(new Subscriber<I>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                s.onSubscribe(subscription);
            }

            @Override
            public void onNext(I i) {
                s.onNext(mapper.apply(i));
            }

            @Override
            public void onError(Throwable t) {
                s.onError(t);
            }

            @Override
            public void onComplete() {
                s.onComplete();
            }
        });
    }
}
