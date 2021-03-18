package org.harvanir.demo.datasource.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.harvanir.demo.datasource.support.DataSourceContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;

/**
 * @author Harvan Irsyadi
 * @see EnableTransactionManagement#order()
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class RoutingDataSourceTransactionalAspect {

    private Object routeAndProceed(ProceedingJoinPoint proceedingJoinPoint, String dataSourceRouteKey, String previousRouteKey) throws Throwable {
        try {
            DataSourceContextHolder.setRouteKey(dataSourceRouteKey);

            return proceedingJoinPoint.proceed();
        } finally {
            if (previousRouteKey == null) {
                DataSourceContextHolder.clear();
            } else {
                if (!previousRouteKey.equals(dataSourceRouteKey)) {
                    DataSourceContextHolder.setRouteKey(previousRouteKey);
                }
            }
        }
    }

    @Around("@annotation(transactional)")
    Object around(ProceedingJoinPoint proceedingJoinPoint, RoutingDataSourceTransactional transactional) throws Throwable {
        String dataSourceRouteKey = transactional.dataSourceRouteKey();

        if (RouteContext.ALWAYS_NEW.equals(transactional.routeContext())) {
            String previousRouteKey = DataSourceContextHolder.getRouteKey();
            validateRouteKey(dataSourceRouteKey);

            return routeAndProceed(proceedingJoinPoint, dataSourceRouteKey, previousRouteKey);
        } else if (RouteContext.REQUIRED.equals(transactional.routeContext())) {
            validateRouteKey(dataSourceRouteKey);

            String previousRouteKey = DataSourceContextHolder.getRouteKey();
            if (previousRouteKey == null) {
                return routeAndProceed(proceedingJoinPoint, dataSourceRouteKey, null);
            } else if (!dataSourceRouteKey.equals(previousRouteKey)) {
                return routeAndProceed(proceedingJoinPoint, dataSourceRouteKey, previousRouteKey);
            }
        }

        return proceedingJoinPoint.proceed();
    }

    private void validateRouteKey(String dataSourceRouteKey) {
        if (ObjectUtils.isEmpty(dataSourceRouteKey) || dataSourceRouteKey.contains(" ")) {
            throw new InvalidRouteKeyException(String.format("Invalid data source routing key: \"%s\"", dataSourceRouteKey));
        }
    }
}
