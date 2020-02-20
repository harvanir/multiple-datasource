package org.harvanir.demo.datasource.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Harvan Irsyadi
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.datasource")
public class DataSourceRoutingProperties {

    private boolean lenientFallback = true;
}
