package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

public class Monoo<T> implements Publisher<T> {
    private final T value;

    public Monoo(T value) {
        this.value = value;
    }

    public static <T> Publisher<T> just(T value) {
        return new Monoo<T>(value);
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(new Subscription() {
            boolean done;

            @Override
            public void request(long n) {
                if (done) {
                    return;
                }

                s.onNext(value);
                s.onComplete();
                done = true;
            }

            @Override
            public void cancel() {
                done = true;
            }
        });
    }
}
