package com.github.kevinjom.rx.example;

import org.junit.jupiter.api.Test;
import org.slf4j.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

public class MonooBlockTest {

    Logger logger = LoggerFactory.getLogger(MonooBlockTest.class);

    @Test
    void mono_testBlcok() {
        // Mono
        assertThat(
                Mono.just(1)
                        .map(i -> {
                            logger.info("sleeping...");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                            }
                            logger.info("finished sleeping...");
                            return i * 3;
                        })
                        .block()
        ).isEqualTo(3);
    }


    @Test
    void monoo_testBlcok() {
        // Monoo
        assertThat(
                Monoo.just(1)
                        .map(i -> {
                            logger.info("sleeping...");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                            }
                            logger.info("finished sleeping...");
                            return i * 3;
                        })
                        .block()
        ).isEqualTo(3);
    }


    @Test
    void mono_testBlockError() {
        // Mono
        assertThatThrownBy(
                () -> Mono.just(1)
                        .map(i -> {
                            logger.info("sleeping...");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                            }
                            logger.info("finished sleeping...");
                            throw new RuntimeException("mapper ex");
                        })
                        .block()
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void monoo_testBlockError() {
        // Mono
        assertThatThrownBy(
                () -> Monoo.just(1)
                        .map(i -> {
                            logger.info("sleeping...");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                            }
                            logger.info("finished sleeping...");
                            throw new RuntimeException("mapper ex");
                        })
                        .block()
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void failsIfBlockOnNonBlockingThread() throws InterruptedException {
        ExecutorService executor =
                Executors.newSingleThreadExecutor(r -> new NonBlockingThread(r, "non-blocking-thread"));

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            assertThatThrownBy(
                    () -> Monoo.just(1)
                            .block()
            ).isInstanceOf(IllegalStateException.class);

            latch.countDown();
        });

        latch.await();
    }

    @Test
    void mono_failsIfBlockOnNonBlockingThread() throws InterruptedException {
        ExecutorService executor =
                Executors.newSingleThreadExecutor(r -> new NonBlockingThread(r, "non-blocking-thread"));

        CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            assertThatThrownBy(
                    () -> Mono.just(1)
                            .block()
            ).isInstanceOf(IllegalStateException.class);

            latch.countDown();
        });

        latch.await();
    }
}
