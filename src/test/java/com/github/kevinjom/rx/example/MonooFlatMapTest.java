package com.github.kevinjom.rx.example;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reactivestreams.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class MonooFlatMapTest {


    @ParameterizedTest
    @MethodSource("publishers")
    void testFlatMap(Publisher<String> publisher) throws InterruptedException {


        List<String> invocation = new ArrayList<>();

        AtomicReference<Subscription> sub = new AtomicReference<>();

        publisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                invocation.add("onSubscribe");
                sub.set(s);
            }

            @Override
            public void onNext(String value) {
                invocation.add("onNext:" + value);

            }

            @Override
            public void onError(Throwable t) {
                invocation.add("onError");

            }

            @Override
            public void onComplete() {
                invocation.add("onComplete");
            }
        });

        assertThat(invocation).containsExactly(
                "onSubscribe"
        );

        sub.get().request(1);


        Thread.sleep(50);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onNext:hi2",
                "onComplete"
        );

        sub.get().request(1);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onNext:hi2",
                "onComplete"
        );
    }

    @ParameterizedTest
    @MethodSource("failedPublishers")
    void testFlatMapError(Publisher<String> publisher) throws InterruptedException {


        List<String> invocation = new ArrayList<>();

        AtomicReference<Subscription> sub = new AtomicReference<>();

        publisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                invocation.add("onSubscribe");
                sub.set(s);
            }

            @Override
            public void onNext(String value) {
                invocation.add("onNext:" + value);

            }

            @Override
            public void onError(Throwable t) {
                invocation.add("onError");

            }

            @Override
            public void onComplete() {
                invocation.add("onComplete");
            }
        });

        assertThat(invocation).containsExactly(
                "onSubscribe"
        );

        sub.get().request(1);


        Thread.sleep(50);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onError"
        );

        sub.get().request(1);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onError"
        );
    }


    static List<Publisher<String>> publishers() {
        return List.of(
                Mono.just(2).flatMap(i -> anotherAsyncMono(i, false)).map(Function.identity()),
                Monoo.just(2).flatMap(i -> anotherAsyncMonoo(i, false)).map(Function.identity())
        );
    }

    static List<Publisher<String>> failedPublishers() {
        return List.of(
                Mono.just(2).flatMap(i -> anotherAsyncMono(i, true)).map(Function.identity()),
                Monoo.just(2).flatMap(i1 -> anotherAsyncMonoo(i1, true)).map(Function.identity())
        );
    }

    static Monoo<String> anotherAsyncMonoo(int i, boolean fail) {
        return new Monoo<>() {
            @Override
            public void subscribe(Subscriber<? super String> s) {
                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        doRequest(fail, s, i);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }

    private static void doRequest(boolean fail, Subscriber<? super String> s, int i) {
        ForkJoinPool.commonPool().execute(() -> {
            System.out.println("calling on next on thread " + Thread.currentThread().getName());
            if (fail) {
                s.onError(new RuntimeException("fail"));
            } else {
                s.onNext("hi" + i);
                s.onComplete();
            }
        });
    }

    static Mono<String> anotherAsyncMono(int i, boolean fail) {
        return new Mono<>() {

            @Override
            public void subscribe(CoreSubscriber<? super String> s) {
                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        doRequest(fail, s, i);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }

}


