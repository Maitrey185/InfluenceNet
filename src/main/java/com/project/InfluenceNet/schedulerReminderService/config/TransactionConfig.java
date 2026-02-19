package com.project.InfluenceNet.schedulerReminderService.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    // JPA / Postgres Transaction Manager
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager jpaTransactionManager(
            EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
