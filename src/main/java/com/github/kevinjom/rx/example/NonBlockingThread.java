package com.github.kevinjom.rx.example;

import reactor.core.scheduler.NonBlocking;

public class NonBlockingThread extends Thread implements NonBlocking {
    public NonBlockingThread(Runnable r, String name) {
        super(r);
        setName(name);
    }
}
