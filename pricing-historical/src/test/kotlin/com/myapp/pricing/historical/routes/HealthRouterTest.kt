package com.myapp.pricing.historical.routes

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Disabled
//@AutoConfigureWebTestClient(timeout = "36000")
@Testcontainers
//@DataR2dbcTest
//@EnableR2dbcRepositories
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class HealthRouterTest {//: H2Config() {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun health() {
        webClient.get()
            .uri("/health")
            .exchange()
            .expectStatus().isOk
    }

//    @TestConfiguration
//    internal class DbConf {
//        @Bean
//        @Primary
//        fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
//            val initializer = ConnectionFactoryInitializer()
//            initializer.setConnectionFactory(connectionFactory)
//            initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
//            return initializer
//        }
//    }

    companion object {
        @Container
        private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14-alpine")
            .withDatabaseName("activity")
            .withUsername("postgres")
            .withPassword("postgres")
        @JvmStatic
        @DynamicPropertySource
        fun databaseProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { postgres.jdbcUrl.replace("jdbc:", "r2dbc:") }
            registry.add("spring.r2dbc.username") { postgres.username }
            registry.add("spring.r2dbc.password") { postgres.password }
        }
    }

}
