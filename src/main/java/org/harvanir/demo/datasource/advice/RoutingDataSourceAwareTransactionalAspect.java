package org.harvanir.demo.datasource.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.harvanir.demo.datasource.support.DataSourceContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * @author Harvan Irsyadi
 */
@Aspect
@Order(0)
public class RoutingDataSourceAwareTransactionalAspect {

    private Object openTheGateAndExecute(ProceedingJoinPoint proceedingJoinPoint, String dataSourceRouteKey) throws Throwable {
        try {
            DataSourceContextHolder.setRouteKey(dataSourceRouteKey);

            return proceedingJoinPoint.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    @Around("@annotation(transactional)")
    Object around(ProceedingJoinPoint proceedingJoinPoint, RoutingDataSourceAwareTransactional transactional) throws Throwable {
        String dataSourceRouteKey = transactional.dataSourceRouteKey();

        if (RouteContext.ALWAYS_NEW.equals(transactional.routeContext())) {
            validateRouteKey(dataSourceRouteKey);

            return openTheGateAndExecute(proceedingJoinPoint, dataSourceRouteKey);
        } else if (RouteContext.REQUIRED.equals(transactional.routeContext())) {
            validateRouteKey(dataSourceRouteKey);

            String routeKey = DataSourceContextHolder.getRouteKey();
            if (!dataSourceRouteKey.equals(routeKey)) {
                return openTheGateAndExecute(proceedingJoinPoint, dataSourceRouteKey);
            }
        }

        return proceedingJoinPoint.proceed();
    }

    private void validateRouteKey(String dataSourceRouteKey) {
        if (StringUtils.isEmpty(dataSourceRouteKey) || dataSourceRouteKey.contains(" ")) {
            throw new InvalidRouteKeyException(String.format("Invalid data source routing key: \"%s\"", dataSourceRouteKey));
        }
    }
}
