package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.R2dbcConfiguration
import com.myapp.pricing.service.codemapping.db.entity.Currencies
import com.myapp.pricing.service.codemapping.db.entity.ExchangeCurrencyMappings
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import com.myapp.pricing.service.codemapping.db.repository.CurrenciesRepository
import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepository
import com.myapp.pricing.service.codemapping.scheduler.CurrenciesCodeMapperScheduler
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import


/*
[INFO] Running com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepositoryTest
t.a.d.r.DataR2dbcTestContextBootstrapper : Neither @ContextConfiguration nor @ContextHierarchy found for test class [com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepositoryTest], using SpringBootContextLoader
o.s.t.c.support.AbstractContextLoader    : Could not detect default resource locations for test class [com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepositoryTest]: no resource found for suffixes {-context.xml, Context.groovy}.
t.c.s.AnnotationConfigContextLoaderUtils : Could not detect default configuration classes for test class [com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepositoryTest]: ExchangeCurrencyMappingsRepositoryTest does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
t.a.d.r.DataR2dbcTestContextBootstrapper : Using ContextCustomizers: [[ImportsContextCustomizer@5f9a8ddc key = [org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration, org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration, org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration, org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration, org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration, org.springframework.boot.autoconfigure.r2dbc.R2dbcTransactionManagerAutoConfiguration, org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration, org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration, com.myapp.pricing.service.codemapping.R2dbcConfiguration]], org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@1205bd62, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@6337c201, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.autoconfigure.OverrideAutoConfigurationContextCustomizerFactory$DisableAutoConfigurationContextCustomizer@198d6542, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@9da1, org.springframework.boot.test.autoconfigure.filter.TypeExcludeFiltersContextCustomizer@351584c0, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@176b75f7]
.b.t.c.SpringBootTestContextBootstrapper : Found @SpringBootConfiguration com.myapp.pricing.service.codemapping.ServiceCodeMappingApplication for test class com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepositoryTest
t.a.d.r.DataR2dbcTestContextBootstrapper : Using TestExecutionListeners: [org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener@1280bae3, org.springframework.test.context.event.ApplicationEventsTestExecutionListener@256a5df0, org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener@1868ed54, org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener@131777e8, org.springframework.test.context.support.DirtiesContextTestExecutionListener@45790cb, org.springframework.test.context.transaction.TransactionalTestExecutionListener@73a5d86c, org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener@2cf88901, org.springframework.test.context.event.EventPublishingTestExecutionListener@4780341, org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener@3c910acd, org.springframework.boot.test.autoconfigure.restdocs.RestDocsTestExecutionListener@612ac38b, org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerResetTestExecutionListener@9f2fe2e, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrintOnlyOnFailureTestExecutionListener@4dd4965a, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverTestExecutionListener@79273a4f, org.springframework.boot.test.autoconfigure.webservices.client.MockWebServiceServerTestExecutionListener@4e26987b]
 */
//@Disabled("issue on ci, need to drop db on each test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataR2dbcTest
@Import(value = [com.myapp.pricing.service.codemapping.R2dbcConfiguration::class])
internal class CurrenciesCodeMapperLoaderTest {

    // disabled
    @MockK
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    @MockK
    lateinit var entitiesRepository: EntitiesRepository

    @MockK
    lateinit var currenciesCodeMapperScheduler: CurrenciesCodeMapperScheduler

    @MockK
    lateinit var exchangeCodeMapperScheduler: CurrenciesCodeMapperScheduler
    //

    @Autowired
    lateinit var currenciesRepository: CurrenciesRepository

    @Autowired
    lateinit var exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository

    val currenciesCodeMapperLoader by lazy { CurrenciesCodeMapperLoader(currenciesRepository, exchangeCurrencyMappingsRepository) }

    @Test
    fun load() {
        // given
        val cryptoExchangeId: Long = 99
        currenciesRepository.saveAll(listOf(Currencies(null, "1INCH"), Currencies(null, "SHIB"), Currencies(null, "DOT")))
            .collectList().block()
        exchangeCurrencyMappingsRepository.saveAll(
            listOf(
                ExchangeCurrencyMappings(null, cryptoExchangeId, currenciesRepository.findByIsoCode("SHIB").block()!!.id!!, null),
                ExchangeCurrencyMappings(null, cryptoExchangeId, currenciesRepository.findByIsoCode("DOT").block()!!.id!!, "PDOT"),
            )
        ).collectList().block()

        // when
        val res = currenciesCodeMapperLoader.load(cryptoExchangeId).block()

        // then
        assertThat(res).hasSize(1)
        assertThat(res!!["dot"]).isEqualTo("pdot")
    }
}
