package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.CryptoExchanges
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import reactor.test.StepVerifier

@Disabled("issue on ci, need to drop db on each test")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataR2dbcTest
@Import(value = [com.myapp.pricing.service.codemapping.R2dbcConfiguration::class])
class CryptoExchangesRepositoryTest {

    @Autowired
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    private val defaultValue = "cryptoExchange" // from schema.sql

    @Test
    @Order(1)
    fun findAll() {
        cryptoExchangesRepository.findAll()
            .`as`(StepVerifier::create)
            .expectNextMatches { it.kaikoExchangeCode == defaultValue }
            .expectNextMatches { it.kaikoExchangeCode == null }
            .verifyComplete()
    }

    @Test
    @Order(2)
    fun findByKaikoExchangeCodeNotNull() {
        // given
        val value1 = "uni"
        val value2 = "sol"
        val value3 = "ltc"
        val uni = CryptoExchanges(null, value1)
        val sol = CryptoExchanges(null, value2)
        val ltc = CryptoExchanges(null, value3)
//        val null = CryptoExchanges(null, null)
        cryptoExchangesRepository.saveAll(listOf(uni, sol, ltc)).collectList().block()

        // when
        val res = cryptoExchangesRepository.findByKaikoExchangeCodeNotNull()

        // then
        res.`as`(StepVerifier::create)
            .expectNextMatches { it.kaikoExchangeCode == defaultValue }
            .expectNextMatches { it.kaikoExchangeCode == value1 }
            .expectNextMatches { it.kaikoExchangeCode == value2 }
            .expectNextMatches { it.kaikoExchangeCode == value3 }
            .verifyComplete()
    }

}
