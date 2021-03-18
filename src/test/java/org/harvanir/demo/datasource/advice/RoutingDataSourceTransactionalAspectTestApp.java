package org.harvanir.demo.datasource.advice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;

/**
 * @author Harvan Irsyadi
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
        FlywayAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class, HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class, JacksonAutoConfiguration.class, JtaAutoConfiguration.class,
        MultipartAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class, RestTemplateAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class, SpringDataWebAutoConfiguration.class,
        TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class,
        TransactionAutoConfiguration.class, ValidationAutoConfiguration.class, WebMvcAutoConfiguration.class,
        WebSocketServletAutoConfiguration.class
})
public class RoutingDataSourceTransactionalAspectTestApp {
}