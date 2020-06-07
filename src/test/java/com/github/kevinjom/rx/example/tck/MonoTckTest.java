package com.github.kevinjom.rx.example.tck;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.*;
import reactor.core.publisher.Mono;

public class MonoTckTest extends PublisherVerification<Integer> {
    public MonoTckTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<Integer> createPublisher(long elements) {
        return Mono.just(1);
    }

    @Override
    public Publisher<Integer> createFailedPublisher() {
        return null;
    }
}
