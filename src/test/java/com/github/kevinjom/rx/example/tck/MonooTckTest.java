package com.github.kevinjom.rx.example.tck;

import com.github.kevinjom.rx.example.Monoo;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.*;

public class MonooTckTest extends PublisherVerification<Integer> {
    public MonooTckTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<Integer> createPublisher(long elements) {
        return Monoo.just(1);
    }

    @Override
    public Publisher<Integer> createFailedPublisher() {
        return null;
    }
}
