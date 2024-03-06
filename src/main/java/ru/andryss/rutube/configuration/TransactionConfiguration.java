package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class TransactionConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
        return transactionManager;
    }

}
