package vn.softdreams.easypos.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DbType> currentDataSourceType = new ThreadLocal<>();

    public static void setCurrentDataSourceType(DbType dataSourceType) {
        currentDataSourceType.set(dataSourceType);
    }

    public static void clearCurrentDataSourceType() {
        currentDataSourceType.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return currentDataSourceType.get() == null ? DbType.WRITE : currentDataSourceType.get();
    }
}
