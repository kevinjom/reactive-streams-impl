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
        this.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(T t) {
                if (valueConsumer != null) {
                    try {
                        valueConsumer.accept(t);
                    } catch (Throwable e) {
                        propogateError(e);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                if (errorConsumer != null) {
                    errorConsumer.accept(t);
                } else {
                    propogateError(t);
                }
            }

            private void propogateError(Throwable t) {
                throw new RuntimeException("unhandled error", t);
            }

            @Override
            public void onComplete() {
                if (completeConsumer != null) {
                    try {
                        completeConsumer.run();
                    } catch (Throwable e) {
                        propogateError(e);
                    }
                }
            }
        });
    }

}
