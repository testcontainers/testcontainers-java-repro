package org.testcontainers.repro;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.utility.DockerImageName;

public class ReproExampleTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReproExampleTest.class);

    @Test
    public void demoKafka() {
        try (
            // customize the creation of a container as required
            KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
        ) {
            kafka.start();
            assertTrue(kafka.isRunning());

            // ...
        }
    }

    @Test
    public void demoRedis() {
        try (
            // customize the creation of a container as required
            GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.0.5"))
                    .withExposedPorts(6379)
        ) {
            redis.start();
            assertTrue(redis.isRunning());

            // ...
        }
    }

    @Test
    public void demoSelenium() {
        try (
            // customize the creation of a container as required
            BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer().withCapabilities(new ChromeOptions())
                    
        ) {
            chrome.start();
            assertTrue(chrome.isRunning());
            // ...
        }
    }

    @Test
    public void demoSeleniumSkippingRecording() {
        try (
            // customize the creation of a container as required
            BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer().withCapabilities(new ChromeOptions())
                    .withRecordingMode(org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
                    
        ) {
            chrome.start();
            assertTrue(chrome.isRunning());
            // ...
        }
    }
}
