package org.testcontainers.repro;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class ReproExampleTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReproExampleTest.class);

    /**
     * Placeholder for a piece of code that demonstrates the bug. You can use this as a starting point, or replace
     * entirely.
     * <p>
     * Ideally this would be a failing test. If it's excessively difficult to form as a test (e.g. relates to log
     * output, teardown or other side effects) then it would be sufficient to explain the behaviour in the issue
     * description.
     */
    @Test
    public void demonstration() {
        try (
            // customize the creation of a container as required
            KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
        ) {
            kafka.start();

            // ...
        }
    }
}
