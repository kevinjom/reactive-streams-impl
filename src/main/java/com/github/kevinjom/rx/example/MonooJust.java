package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

public class MonooJust<T> extends Monoo<T> {
    private final T value;

    public MonooJust(T value) {
        this.value = value;
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(new Subscription() {
            boolean done;

            @Override
            public void request(long n) {
                if (n <= 0) {
                    s.onError(new IllegalArgumentException());
                    done = true;
                }

                if (done) {
                    return;
                }

                done = true;
                s.onNext(value);
                s.onComplete();
            }

            @Override
            public void cancel() {
                done = true;
            }
        });
    }

}
