package com.github.kevinjom.rx.example.tck;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.*;
import reactor.core.publisher.Mono;

public class MonoTckTest extends PublisherVerification<String> {
    public MonoTckTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<String> createPublisher(long elements) {
        return Mono.just("1");
    }

    @Override
    public Publisher<String> createFailedPublisher() {
        return Mono.just(1)
                .map(i -> {
                    throw new RuntimeException("mapper failed...");
                })
                .map(i -> "hi" + i);
    }
}
