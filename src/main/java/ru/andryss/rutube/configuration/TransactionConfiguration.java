package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfiguration {

//    НЕ РАБОТАЕТ ?????
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JtaTransactionManager transactionManager = new JtaTransactionManager();
//        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
//        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
//        transactionManager.setTransactionSynchronizationRegistryName("java:jboss/TransactionSynchronizationRegistry");
//        return transactionManager;
//    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
        transactionManager.setTransactionSynchronizationRegistryName("java:jboss/TransactionSynchronizationRegistry");
        transactionManager.setAllowCustomIsolationLevels(true);
        transactionManager.afterPropertiesSet();
        TransactionDefinition defaultTransactionDefinition = new TransactionDefinition() {
            @Override
            public int getIsolationLevel() {
                return ISOLATION_REPEATABLE_READ;
            }
        };
        return new TransactionTemplate(transactionManager, defaultTransactionDefinition);
    }

}
