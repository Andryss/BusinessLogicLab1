package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ;

@Configuration
@EnableRetry
public class TransactionConfiguration {

    @Bean
    public TransactionTemplate transactionTemplate() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
        transactionManager.setTransactionSynchronizationRegistryName("java:jboss/TransactionSynchronizationRegistry");
        transactionManager.setAllowCustomIsolationLevels(true);
        transactionManager.afterPropertiesSet();
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setIsolationLevel(ISOLATION_REPEATABLE_READ);
        return template;
    }

}
