package vn.softdreams.easypos.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vn.softdreams.easypos.config.DbType;
import vn.softdreams.easypos.config.RoutingDataSource;

import javax.sql.DataSource;

@Aspect
@Component
@EnableTransactionManagement
public class DataSourceAspect {

    private final AbstractRoutingDataSource abstractRoutingDataSource;

    @Autowired
    @Qualifier("dataSourceReadOnly")
    private DataSource readDataSource;

    @Autowired
    @Qualifier("dataSource")
    private DataSource writeDataSource;

    public DataSourceAspect(AbstractRoutingDataSource abstractRoutingDataSource) {
        this.abstractRoutingDataSource = abstractRoutingDataSource;
    }

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethod() {}

    @Before("transactionalMethod()")
    public void setDataSource(JoinPoint joinPoint) {
        RoutingDataSource routingDataSource = (RoutingDataSource) abstractRoutingDataSource;
        final boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (readOnly) {
            routingDataSource.setCurrentDataSourceType(DbType.READ_ONLY);
        } else {
            routingDataSource.setCurrentDataSourceType(DbType.WRITE);
        }
    }

    @After("transactionalMethod()")
    public void clearDataSource(JoinPoint joinPoint) {
        RoutingDataSource.clearCurrentDataSourceType();
    }
}
