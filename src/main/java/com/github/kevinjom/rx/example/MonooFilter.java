package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.function.Predicate;

public class MonooFilter<T> extends Monoo<T> {
    private final Monoo source;
    private final Predicate<T> predicate;

    public MonooFilter(Monoo source, Predicate<T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
        source.subscribe(new MonooFilterSubscriber(s, predicate));
    }

    private static class MonooFilterSubscriber<T> implements Subscriber<T> {
        private final Subscriber<? super T> actual;
        private final Predicate<T> predicate;
        private boolean done;

        public MonooFilterSubscriber(Subscriber<? super T> actual, Predicate<T> predicate) {
            this.actual = actual;
            this.predicate = predicate;
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
        public void onNext(T o) {
            try {
                boolean pass = predicate.test(o);
                if (pass) {
                    actual.onNext(o);
                } else {
                    onComplete();
                }
            } catch (Throwable throwable) {
                onError(throwable);
            }
        }
    }
}
