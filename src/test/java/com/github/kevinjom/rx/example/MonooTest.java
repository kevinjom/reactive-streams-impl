package com.github.kevinjom.rx.example;

import org.junit.jupiter.api.Test;
import org.reactivestreams.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class MonooTest {

    @Test
    void testPublisher() {
        Publisher<Integer> publisher = Monoo.just(1);


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
}
