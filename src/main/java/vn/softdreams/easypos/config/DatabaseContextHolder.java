package vn.softdreams.easypos.config;

public class DatabaseContextHolder {

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDatabaseType(DbType type) {
        contextHolder.set(type);
    }

    public static DbType getDatabaseType() {
        return contextHolder.get();
    }

    public static void clearDatabaseType() {
        contextHolder.remove();
    }
}
