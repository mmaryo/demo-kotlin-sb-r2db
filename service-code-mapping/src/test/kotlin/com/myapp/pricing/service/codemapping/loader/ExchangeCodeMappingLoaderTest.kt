package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.CryptoExchanges
import com.myapp.pricing.service.codemapping.db.entity.Entities
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import com.myapp.pricing.service.codemapping.db.repository.CurrenciesRepository
import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepository
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import


//@Disabled("issue on ci, need to drop db on each test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataR2dbcTest
@Import(value = [com.myapp.pricing.service.codemapping.R2dbcConfiguration::class])
internal class ExchangeCodeMappingLoaderTest {

    // disabled
    @MockK
    lateinit var currenciesRepository: CurrenciesRepository

    @MockK
    lateinit var exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository
    //

    @Autowired
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    @Autowired
    lateinit var entitiesRepository: EntitiesRepository

    val loader by lazy { ExchangeCodeMappingLoader(cryptoExchangesRepository, entitiesRepository) }

    @Test
    fun build() {
        // given
        val defaultValue = "cryptoExchange" // from schema.sql
        val binc = cryptoExchangesRepository.save(CryptoExchanges(null, "binc")).block()
        val ftx = cryptoExchangesRepository.save(CryptoExchanges(null, "ftxx")).block()
        cryptoExchangesRepository.save(CryptoExchanges(null, "okex")).block()

        entitiesRepository.save(Entities(null, "", "")).block() // need to create Entities in order to update them
        entitiesRepository.save(Entities(null, "", "")).block() // need to create Entities in order to update them
        entitiesRepository.save(Entities(null, "", "")).block() // need to create Entities in order to update them

        val bincId = binc!!.id
        val ftxId = ftx!!.id
        entitiesRepository.save(Entities(bincId, "Binance", "bnc")).block()
        entitiesRepository.save(Entities(ftxId, "FTX", "ftx")).block()

        // when
        val res = loader.loadIdsToKaiko().block()

        // then
        assertThat(res!!["1"]).isEqualTo(defaultValue)
        assertThat(res[bincId.toString()]).isEqualTo(binc.kaikoExchangeCode)
        assertThat(res[ftxId.toString()]).isEqualTo(ftx.kaikoExchangeCode)
    }

}

