package com.github.kevinjom.rx.example;

import org.reactivestreams.*;

import java.util.function.*;

public class Monoo<T> implements Publisher<T> {
    private final T value;

    public Monoo(T value) {
        this.value = value;
    }

    public static <T> Monoo<T> just(T value) {
        return new Monoo<T>(value);
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

    //TODO: return handle for cancellation
    public void subscribe(Consumer<T> valueConsumer, Consumer<Throwable> errorConsumer, Runnable completeConsumer) {
        this.subscribe(new LambdaMonooSubscriber<>(valueConsumer, errorConsumer, completeConsumer));
    }

}
