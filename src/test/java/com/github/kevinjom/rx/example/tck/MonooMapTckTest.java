package com.github.kevinjom.rx.example.tck;

import com.github.kevinjom.rx.example.Monoo;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.*;

public class MonooMapTckTest extends PublisherVerification<String> {
    public MonooMapTckTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<String> createPublisher(long elements) {
        return Monoo.just(1).map(i -> i * 2).map(i -> "hi" + i);
    }

    @Override
    public Publisher<String> createFailedPublisher() {
        return null;
    }
}
