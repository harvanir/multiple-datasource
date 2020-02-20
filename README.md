# Getting Started

## Application properties
```yaml
spring:
  datasource:
    name: ds_1
    username: root
    password: password
    url: jdbc:mysql://localhost:3306/ds_1?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta
    hikari:
      connection-test-query: SELECT 1;
      connection-timeout: 30000
      driver-class-name: com.mysql.cj.jdbc.Driver
      idle-timeout: 600000
      max-lifetime: 1800000
      maximum-pool-size: 30
      minimum-idle: 5
      validation-timeout: 28000
  jpa:
    database-platform: org.hibernate.dialect.MySQL57InnoDBDialect
    generate-ddl: false
    hibernate:
      ddl-auto: none
      use-new-id-generator-mappings: true
    open-in-view: false
    properties:
      hibernate.format_sql: true
    show-sql: false
app:
  datasource:
    lenient-fallback: true
  datasources:
    - name: ds_2
      username: root
      password: password
      url: jdbc:mysql://localhost:3306/ds_2?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta
      hikari:
        connection-test-query: SELECT 1;
        connection-timeout: 30000
        driver-class-name: com.mysql.cj.jdbc.Driver
        idle-timeout: 600000
        max-lifetime: 1800000
        maximum-pool-size: 30
        minimum-idle: 5
        validation-timeout: 28000
    - name: ds_3
      username: root
      password: password
      url: jdbc:mysql://localhost:3306/ds_3?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta
      hikari:
        connection-test-query: SELECT 1;
        connection-timeout: 30000
        driver-class-name: com.mysql.cj.jdbc.Driver
        idle-timeout: 600000
        max-lifetime: 1800000
        maximum-pool-size: 30
        minimum-idle: 5
        validation-timeout: 28000
```
## Configuration
* Import <code>org.harvanir.demo.datasource.configuration.RoutingDataSourceConfiguration</code>, <code>org.harvanir.demo.datasource.configuration.TransactionRoutingDataSourceConfiguration</code> class to your spring boot configuration.
    ```java
    import org.harvanir.demo.datasource.configuration.AppConfiguration;
    import org.harvanir.demo.datasource.configuration.RoutingDataSourceConfiguration;
    import org.harvanir.demo.datasource.configuration.TransactionRoutingDataSourceConfiguration;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Import;
    
    @Import({AppConfiguration.class, RoutingDataSourceConfiguration.class, TransactionRoutingDataSourceConfiguration.class})
    @Configuration
    public class YourConfiguration {
    }
    ```
* Or annotate your spring boot configuration class using <code>org.harvanir.demo.datasource.configuration.EnableRoutingDataSource</code>, <code>org.harvanir.demo.datasource.configuration.EnableTransactionRoutingDataSource</code>
    ```java
    import org.harvanir.demo.datasource.configuration.EnableRoutingDataSource;
    import org.harvanir.demo.datasource.configuration.EnableTransactionRoutingDataSource;
    import org.springframework.context.annotation.Configuration;
      
    @EnableRoutingDataSource
    @EnableTransactionRoutingDataSource
    @Configuration
    public class YourConfiguration {
    }
    ```
## Usage
### Programmatic
```java
public class Service {
    
    private final SomeJpaRepsitory someJpaRepository;

    public Service(SomeJpaRepository someJpaRepository) {
        this.someJpaRepository = someJpaRepository;
    }
    
    public void someMethod() {
        try {
            String dataSourceKey = "ds_2"; // Based on our application properties, the key should be around ("ds_2", "ds_3");
            DataSourceContextHolder.setRouteKey(dataSourceKey);
            
            SomeJpaModel model = new SomeJpaModel();
            model.setId(UUID.randomUUID().toString());
            
            someJpaRepository.save(model);
            Optional<SomeJpaModel> optional = someJpaRepository.findById(model.getId());
            
            if (!optional.isPresent()) {
                throw new Exception("This is something impossible...");
            }
        } finally {
            DataSourceContextHolder.clear();
        }
    }
}
```
### Annotation based
```java
@org.springframework.stereotype.Service
public class Service {
    
    private final SomeJpaRepsitory someJpaRepository;

    public Service(SomeJpaRepository someJpaRepository) {
        this.someJpaRepository = someJpaRepository;
    }
    
    @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
    public void someMethod() {
        SomeJpaModel model = new SomeJpaModel();
        model.setId(UUID.randomUUID().toString());
        
        someJpaRepository.save(model);
        Optional<SomeJpaModel> optional = someJpaRepository.findById(model.getId());
        
        if (!optional.isPresent()) {
            throw new Exception("This is something impossible...");
        }
    }
}
```