package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableRetry
@EnableTransactionManagement
public class TransactionConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
        transactionManager.setTransactionSynchronizationRegistryName("java:jboss/TransactionSynchronizationRegistry");
        transactionManager.setAllowCustomIsolationLevels(true);
        transactionManager.afterPropertiesSet();
        return transactionManager;
    }

}
