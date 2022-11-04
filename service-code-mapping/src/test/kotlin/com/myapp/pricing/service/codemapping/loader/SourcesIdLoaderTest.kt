package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.Entities
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import com.myapp.pricing.service.codemapping.db.repository.CurrenciesRepository
import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepository
import com.myapp.pricing.service.codemapping.loader.config.SourcesIdConfig
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
internal class SourcesIdLoaderTest {

    // disabled
    @MockK
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    @MockK
    lateinit var currenciesRepository: CurrenciesRepository

    @MockK
    lateinit var exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository
    //

    @Autowired
    lateinit var entitiesRepository: EntitiesRepository

    val sourcesIdLoader by lazy { SourcesIdLoader(entitiesRepository) }

    @Test
    fun sources() {
        // given
        entitiesRepository.save(Entities(null, SourcesIdConfig.sourceKaikoCode)).block()
        entitiesRepository.save(Entities(null, SourcesIdConfig.sourceCoinMarketCapCode)).block()

        // when
        val res = sourcesIdLoader.sourcesId().block()!!

        // then
        assertThat(res.sourceKaikoId).isPositive
        assertThat(res.sourceCoinMarketCapId).isPositive
    }

}
