package com.github.kevinjom.rx.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reactivestreams.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MonooTest {

    @ParameterizedTest
    @MethodSource("publishers")
    void testPublisher(Publisher<Integer> publisher) {


        List<String> invocation = new ArrayList<>();

        AtomicReference<Subscription> sub = new AtomicReference<>();

        publisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                invocation.add("onSubscribe");
                sub.set(s);
            }

            @Override
            public void onNext(Integer integer) {
                invocation.add("onNext:" + integer);

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

    static List<Publisher<Integer>> publishers() {
        return List.of(
                Mono.just(1),
                Monoo.just(1)
        );
    }
}
