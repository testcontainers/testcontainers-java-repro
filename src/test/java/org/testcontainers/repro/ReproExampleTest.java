package org.testcontainers.repro;

import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReproExampleTest {

    @Test
    public void bindMountLogFiles() throws IOException, InterruptedException {
        try (
                GenericContainer<?> container = new GenericContainer<>("nginx:1.19.0-alpine")
                        .withExposedPorts(80)
                        .withFileSystemBind(System.getProperty("user.dir") + "/nginx",
                                "/var/log/nginx", BindMode.READ_WRITE)
        ) {
            container.start();

            // request against existing path
            performRequestAgainstContainer(container, "/");

            // request against nonsense path
            long now = System.currentTimeMillis();
            performRequestAgainstContainer(container, "/foobar/" + now);

            // manually write a file into the container
            Container.ExecResult result = container.execInContainer("/bin/sh",  "-c", "echo '" + now + " Hello, World!' >> /var/log/nginx/test.log");

            // check that nginx/error.log in the project directory contains the now timestamp
            String errorLog = System.getProperty("user.dir") + "/nginx/error.log";
            String errorLogContents = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(errorLog)));

            String testLog = System.getProperty("user.dir") + "/nginx/test.log";
            String testLogContents = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(testLog)));
            Assert.assertTrue(testLogContents.contains(Long.toString(now)));

        }

    }

    @Test
    public void postgresContainerWithBootstrapFiles() throws Exception {
        try (
                PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.1")
                        .withClasspathResourceMapping("init.sql", "/docker-entrypoint-initdb.d/init.sql", BindMode.READ_WRITE)
        ) {
            postgres.start();

            try (
                    Connection conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM test_table")
            ) {
                // count all rows in result set
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                }

                // assert row count is 3
                Assert.assertEquals(3, rowCount);
            }
        }
    }



    private static int performRequestAgainstContainer(GenericContainer<?> container, String path) throws IOException {
        URL url = new URL("http://" + container.getHost() + ":" + container.getMappedPort(80) + path);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn.getResponseCode();
    }
}
