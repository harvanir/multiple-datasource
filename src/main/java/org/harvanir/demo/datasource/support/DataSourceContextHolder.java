package org.harvanir.demo.datasource.support;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Harvan Irsyadi
 */
@Slf4j
public class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    private DataSourceContextHolder() {
    }

    public static String getRouteKey() {
        return CONTEXT.get();
    }

    /**
     * @param value Only for non default data source. Default data source configured via application properties/yml files key-value: <b>spring.datasource.**</b>
     */
    public static void setRouteKey(String value) {
        log.debug("Setting route key: {}", value);

        CONTEXT.set(value);
    }

    public static void clear() {
        log.debug("Remove route key.");

        CONTEXT.remove();
    }
}
