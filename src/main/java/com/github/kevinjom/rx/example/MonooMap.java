package com.github.kevinjom.rx.example;

import org.reactivestreams.Subscriber;

import java.util.function.Function;

public class MonooMap<I, O> extends Monoo<O> {
    private final Monoo<I> source;
    private final Function<I, O> mapper;

    public MonooMap(Monoo<I> source, Function<I, O> mapper) {
        super(); // FIXME: what do we put here?
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super O> s) {

    }
}
