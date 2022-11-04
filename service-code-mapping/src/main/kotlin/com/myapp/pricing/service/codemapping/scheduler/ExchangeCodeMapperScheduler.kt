package com.myapp.pricing.service.codemapping.scheduler

import com.myapp.pricing.service.codemapping.cache.ExchangeCodeMapperCache
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import com.myapp.pricing.service.codemapping.loader.ExchangeCodeMappingLoader
import com.myappapi.SourceType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Component
open class ExchangeCodeMapperScheduler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var cache: ExchangeCodeMapperCache

    @Autowired
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    @Autowired
    lateinit var entities: EntitiesRepository

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.HOURS)
    fun reloadConf() {
        Mono.zip(
            ExchangeCodeMappingLoader(cryptoExchangesRepository, entities)
                .loadIdsToKaiko(),
            ExchangeCodeMappingLoader(cryptoExchangesRepository, entities)
                .loadCodeToKaiko(),
        )
            .doOnSuccess {
                val newValues: MutableMap<String, String> = mutableMapOf()
                newValues.putAll(it.t1)
                newValues.putAll(it.t2)
                cache.set(SourceType.kaiko, newValues)
                log.info("-- Reload ExchangeCodeMapperService with ${newValues.size} Kaiko values")
            }
            .block()
    }

}
