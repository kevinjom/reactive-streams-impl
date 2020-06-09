package com.github.kevinjom.rx.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reactivestreams.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MonooFilterTest {

    @ParameterizedTest
    @MethodSource("publishers")
    void testFilter_pass(Publisher<String> publisher) {


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

        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onNext:1",
                "onComplete"
        );

        sub.get().request(1);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onNext:1",
                "onComplete"
        );
    }


    @ParameterizedTest
    @MethodSource("rejectedPublishers")
    void testFilter_reject(Publisher<String> publisher) {


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

        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onComplete"
        );

        sub.get().request(1);
        assertThat(invocation).containsExactly(
                "onSubscribe",
                "onComplete"
        );
    }


    @ParameterizedTest
    @MethodSource("failedPublishers")
    void testFilterError(Publisher<String> publisher) {


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
                Mono.just("1").filter(i -> i.length() > 0),
                Monoo.just("1").filter(i -> i.length() > 0)
        );
    }

    static List<Publisher<String>> rejectedPublishers() {
        return List.of(
                Mono.just("1").filter(String::isBlank),
                Monoo.just("1").filter(String::isBlank)
        );
    }


    static List<Publisher<String>> failedPublishers() {
        return List.of(
                Mono.just("1").filter(i -> {
                    throw new RuntimeException("mapper failed");
                }),

                Monoo.just("1").filter(i -> {
                    throw new RuntimeException("mapper failed");
                })
        );
    }
}
