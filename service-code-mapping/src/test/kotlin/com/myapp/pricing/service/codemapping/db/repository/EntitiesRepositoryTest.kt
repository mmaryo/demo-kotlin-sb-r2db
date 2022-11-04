package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.Entities
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
class EntitiesRepositoryTest {

    @Autowired
    lateinit var entitiesRepository: EntitiesRepository

    private val defaultValueName = "name" // from schema.sql
    private val defaultValueCode = "code" // from schema.sql

    @Test
    @Order(1)
    fun findAll() {
        entitiesRepository.findAll()
            .`as`(StepVerifier::create)
            .expectNextMatches { it.name == defaultValueName && it.code == defaultValueCode }
            .verifyComplete()
    }

    @Test
    @Order(2)
    fun findByName() {
        // given
        val name = "TestKaiko"
        val kaiko = Entities(null, name)
        val binance = Entities(null, "TestBinance")
        val coinbase = Entities(null, "TestCoinbase")
        val kraken = Entities(null, "TestKraken")
        entitiesRepository.saveAll(listOf(kaiko, binance, coinbase, kraken)).collectList().block()

        // when
        val res = entitiesRepository.findByName(name)

        // then
        res.`as`(StepVerifier::create)
            .expectNextMatches { it.name == kaiko.name }
            .verifyComplete()
    }
}
