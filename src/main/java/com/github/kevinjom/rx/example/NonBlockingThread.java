package com.github.kevinjom.rx.example;

public class NonBlockingThread extends Thread {
    public NonBlockingThread(Runnable r, String name) {
        super(r);
        setName(name);
    }
}
