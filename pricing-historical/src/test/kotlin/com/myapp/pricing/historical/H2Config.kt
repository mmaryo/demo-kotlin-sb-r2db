package com.myapp.pricing.historical

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

open class H2Config {
    @TestConfiguration
    @EnableR2dbcRepositories
    @EnableAutoConfiguration
    internal class LoadSchema {
        @Bean
        fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
            val initializer = ConnectionFactoryInitializer()
            initializer.setConnectionFactory(connectionFactory)
            initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
            return initializer
        }
    }
}
