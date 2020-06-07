package com.github.kevinjom.rx.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reactivestreams.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MonooMapTest {

    @ParameterizedTest
    @MethodSource("publishers")
    void testMap(Publisher<String> publisher) {


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

    static List<Publisher<String>> publishers() {
        return List.of(
                Mono.just(1).map(i -> i * 2).map(i -> "hi" + i),
                Monoo.just(1).map(i -> i * 2).map(i -> "hi" + i)
        );
    }
}
