package org.harvanir.demo.datasource.advice;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Warning!!!</b> The route context mechanism is not <b>thread-safe</b>, please use it in an appropriate manner.<br/><br/>
 * Describes a spring transaction attribute on an individual method or on a class with additional attributes to support routing data source.
 *
 * @author Harvan Irsyadi
 * @see org.springframework.transaction.annotation.Transactional
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
public @interface RoutingDataSourceAwareTransactional {

    @AliasFor(annotation = Transactional.class)
    String value() default "";

    @AliasFor(annotation = Transactional.class)
    String transactionManager() default "";

    @AliasFor(annotation = Transactional.class)
    Propagation propagation() default Propagation.REQUIRED;

    @AliasFor(annotation = Transactional.class)
    Isolation isolation() default Isolation.DEFAULT;

    @AliasFor(annotation = Transactional.class)
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    @AliasFor(annotation = Transactional.class)
    boolean readOnly() default false;

    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] rollbackFor() default {};

    @AliasFor(annotation = Transactional.class)
    String[] rollbackForClassName() default {};

    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] noRollbackFor() default {};

    @AliasFor(annotation = Transactional.class)
    String[] noRollbackForClassName() default {};

    String dataSourceRouteKey();

    RouteContext routeContext() default RouteContext.ALWAYS_NEW;
}
