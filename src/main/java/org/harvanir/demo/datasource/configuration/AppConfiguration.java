package org.harvanir.demo.datasource.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Harvan Irsyadi
 */
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {
}