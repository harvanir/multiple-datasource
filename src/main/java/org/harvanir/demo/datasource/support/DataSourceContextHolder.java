package org.harvanir.demo.datasource.support;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static org.harvanir.demo.datasource.support.DataSourceContextHolder.StringDefer.defer;

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
        log.debug("{}", defer(() -> String.format("Setting route key: %s", value)));

        CONTEXT.set(value);
    }

    public static void clear() {
        log.debug("{}", defer(() -> "Remove route key."));

        CONTEXT.remove();
    }

    static class StringDefer {

        private final Supplier<String> supplier;

        private StringDefer(Supplier<String> supplier) {
            this.supplier = supplier;
        }

        public static StringDefer defer(Supplier<String> supplier) {
            return new StringDefer(supplier);
        }

        @Override
        public String toString() {
            return supplier.get();
        }
    }
}
