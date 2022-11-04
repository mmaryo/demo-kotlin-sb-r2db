package com.myapp.pricing.service.codemapping.loader.config

import com.myapp.pricing.service.codemapping.cache.CurrenciesCodeMapperCache
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CurrenciesCodeMapperConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    @ConditionalOnProperty(value = ["currencies-mapper-service.enable"], havingValue = "true", matchIfMissing = true)
    open fun initCurrenciesMapperService(): CurrenciesCodeMapperCache {
        log.info("-- Load CurrenciesMapperService")
        return CurrenciesCodeMapperCache()
    }

    @Bean
    @ConditionalOnProperty(value = ["currencies-mapper-service.enable"], havingValue = "false")
    open fun emptyCurrenciesMapperService(): CurrenciesCodeMapperCache {
        log.warn("-- Empty CurrenciesMapperService")
        return CurrenciesCodeMapperCache()
    }

}
