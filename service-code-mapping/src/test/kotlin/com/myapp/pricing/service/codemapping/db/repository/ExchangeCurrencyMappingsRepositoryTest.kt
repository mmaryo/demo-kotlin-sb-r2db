package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.ExchangeCurrencyMappings
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
class ExchangeCurrencyMappingsRepositoryTest {

    @Autowired
    lateinit var exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository

    private val defaultCryptoExchangeId = 66L // from schema.sql
    private val defaultCurrencyId = 77L // from schema.sql
    private val defaultValueMapping = "mapping" // from schema.sql

    @Test
    fun findAll() {
        exchangeCurrencyMappingsRepository.findAll()
            .`as`(StepVerifier::create)
            .expectNextMatches { it.cryptoExchangeId == defaultCryptoExchangeId && it.currencyId == defaultCurrencyId && it.mapping == defaultValueMapping }
            .verifyComplete()
    }

    @Test
    fun findByCryptoExchangeId() {
        // given
        val cryptoExchangeId: Long = 1
        val currencyId1: Long = 11
        val currencyId2: Long = 22
        val mapping1 = "dot"
        val mapping2 = "sol"
        val dot = ExchangeCurrencyMappings(null, cryptoExchangeId, currencyId1, mapping1)
        val sol = ExchangeCurrencyMappings(null, cryptoExchangeId, currencyId2, mapping2)
        val uni = ExchangeCurrencyMappings(null, 2, 3, "uni")
        val ltc = ExchangeCurrencyMappings(null, 3, 4, "ltc")
        exchangeCurrencyMappingsRepository.saveAll(listOf(dot, sol, uni, ltc)).collectList().block()

        // when
        val res = exchangeCurrencyMappingsRepository.findByCryptoExchangeId(cryptoExchangeId)

        // then
        res.`as`(StepVerifier::create)
            .expectNextMatches { it.cryptoExchangeId == cryptoExchangeId && it.currencyId == currencyId1 && it.mapping == mapping1 }
            .expectNextMatches { it.cryptoExchangeId == cryptoExchangeId && it.currencyId == currencyId2 && it.mapping == mapping2 }
            .verifyComplete()
    }
}
