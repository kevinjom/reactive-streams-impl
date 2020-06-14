package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.function.Function;

public class MonooFlatMap<I, O> extends Monoo<O> {
    private final Monoo<I> source;
    private final Function<I, Monoo<O>> mapper;

    public MonooFlatMap(Monoo<I> source, Function<I, Monoo<O>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super O> s) {

        source.subscribe(new FlatMapSubscriber<>(s, mapper));
    }

    private static class FlatMapSubscriber<I, O> implements Subscriber<I> {
        private final Subscriber<? super O> actual;
        private final Function<I, Monoo<O>> mapper;
        private boolean done;

        public FlatMapSubscriber(Subscriber<? super O> actual, Function<I, Monoo<O>> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription s) {
            actual.onSubscribe(s);
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                return;
            }

            done = true;
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }

            done = true;
            actual.onComplete();
        }

        @Override
        public void onNext(I o) {
            if (done) {
                return;
            }

            done = true;

            try {
                Monoo<O> then = mapper.apply(o);
                then.subscribe(new Subscriber<>() {
                    boolean thenDone;

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(O o) {
                        if (thenDone) {
                            return;
                        }

                        actual.onNext(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (thenDone) {
                            return;
                        }

                        thenDone = true;
                        actual.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        if (thenDone) {
                            return;
                        }

                        thenDone = true;
                        actual.onComplete();
                    }
                });
            } catch (Throwable throwable) {
                actual.onError(throwable);
            }
        }
    }
}
