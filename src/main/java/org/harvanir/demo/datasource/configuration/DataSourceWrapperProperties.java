package org.harvanir.demo.datasource.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
public class DataSourceWrapperProperties extends DataSourceProperties {

    private Hikari hikari = new Hikari();

    @Getter
    @Setter
    static class Hikari {

        private String connectionTestQuery;

        private long connectionTimeout = SECONDS.toMillis(30);

        private String driverClassName;

        private long idleTimeout = MINUTES.toMillis(10);

        private long maxLifetime = MINUTES.toMillis(30);

        private int maximumPoolSize = -1;

        private int minimumIdle = -1;

        private long validationTimeout = SECONDS.toMillis(5);
    }
}
