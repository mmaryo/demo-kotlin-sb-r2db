package com.myapp.pricing.service.codemapping.scheduler

import com.myappapi.SourceType
import com.myapp.pricing.service.codemapping.cache.CurrenciesCodeMapperCache
import com.myapp.pricing.service.codemapping.db.repository.CurrenciesRepository
import com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepository
import com.myapp.pricing.service.codemapping.loader.CurrenciesCodeMapperLoader
import com.myapp.pricing.service.codemapping.loader.SourcesId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Component
open class CurrenciesCodeMapperScheduler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var cache: CurrenciesCodeMapperCache

    @Autowired
    private lateinit var sourcesId: SourcesId

    @Autowired
    private lateinit var currenciesRepository: CurrenciesRepository

    @Autowired
    private lateinit var exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.HOURS)
//    @Scheduled(cron = "${}") // 0 0 0/12 * * ?
    fun reloadConf() {
        Mono.zip(
            CurrenciesCodeMapperLoader(currenciesRepository, exchangeCurrencyMappingsRepository)
                .load(),
            CurrenciesCodeMapperLoader(currenciesRepository, exchangeCurrencyMappingsRepository)
                .load(sourcesId.sourceKaikoId),
            CurrenciesCodeMapperLoader(currenciesRepository, exchangeCurrencyMappingsRepository)
                .load(sourcesId.sourceCoinMarketCapId)
        )
            .doOnSuccess {
                cache.set(SourceType.any, it.t1)
                cache.set(SourceType.kaiko, it.t2)
                cache.set(SourceType.coinmarketcap, it.t3)
                log.info("-- Reload CurrenciesCodeMapperScheduler with ${it.t1.size} Ids currencies, ${it.t2.size} Kaiko currencies, ${it.t3.size} CoinMarketCap currencies")
            }
            .block()
    }

}
