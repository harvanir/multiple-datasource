package org.harvanir.demo.datasource.support;

import lombok.SneakyThrows;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Harvan Irsyadi
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private Map<Object, DataSource> resolvedDataSources;

    private DataSource resolvedDefaultDataSource;

    private boolean lenientFallback = true;

    @Override
    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
        super.setLenientFallback(lenientFallback);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        mapParentPrivateFields();
    }

    /**
     * We hack something.
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void mapParentPrivateFields() {
        Field resolvedDataSourcesField = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
        resolvedDataSourcesField.setAccessible(true);

        Field resolvedDefaultDataSourceField = AbstractRoutingDataSource.class.getDeclaredField("resolvedDefaultDataSource");
        resolvedDefaultDataSourceField.setAccessible(true);

        this.resolvedDataSources = (Map<Object, DataSource>) resolvedDataSourcesField.get(this);
        this.resolvedDefaultDataSource = (DataSource) resolvedDefaultDataSourceField.get(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = null;

        // Return directly to the default data source when the lookupKey is null.
        if (lookupKey == null) {
            if (lenientFallback) {
                return resolvedDefaultDataSource;
            }
        } else {
            dataSource = resolvedDataSources.get(lookupKey);
            if (dataSource == null && lenientFallback) {
                dataSource = resolvedDefaultDataSource;
            }
        }

        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }

        return dataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getRouteKey();
    }
}
