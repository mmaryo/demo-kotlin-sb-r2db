package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.Currencies
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
class CurrenciesRepositoryTest {

    @Autowired
    lateinit var currenciesRepository: CurrenciesRepository

    private val defaultValue = "cryptoCode" // from schema.sql

    @Test
    @Order(1)
    fun findAll() {
        currenciesRepository.findAll()
            .`as`(StepVerifier::create)
            .expectNextMatches { it.isoCode == defaultValue }
            .verifyComplete()
    }

    @Test
    @Order(2)
    fun findByIsoCode() {
        // given
        val dotIsoCode = "dot"
        val dot = Currencies(null, dotIsoCode)
        val sol = Currencies(null, "sol")
        val uni = Currencies(null, "uni")
        val ltc = Currencies(null, "ltc")
        currenciesRepository.saveAll(listOf(dot, sol, uni, ltc)).collectList().block()

        // when
        val res = currenciesRepository.findByIsoCode(dotIsoCode)

        // then
        res.`as`(StepVerifier::create)
            .expectNextMatches { it.isoCode == dotIsoCode }
            .verifyComplete()
    }
}
