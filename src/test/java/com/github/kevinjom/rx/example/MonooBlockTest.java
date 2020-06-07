package com.github.kevinjom.rx.example;

import org.junit.jupiter.api.Test;
import org.slf4j.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

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
}
