package com.myapp.pricing.service.codemapping.loader.config

import com.myapp.pricing.service.codemapping.cache.ExchangeCodeMapperCache
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ExchangeCodeMapperConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    @ConditionalOnProperty(value = ["exchange-code-mapper-service.enable"], havingValue = "true", matchIfMissing = true)
    open fun initExchangeCodeMapperService(): ExchangeCodeMapperCache {
        log.info("-- Load ExchangeCodeMapperService")
        return ExchangeCodeMapperCache()
    }

    @Bean
    @ConditionalOnProperty(value = ["exchange-code-mapper-service.enable"], havingValue = "false")
    open fun emptyExchangeCodeMapperService(): ExchangeCodeMapperCache {
        log.warn("-- Empty ExchangeCodeMapperService")
        return ExchangeCodeMapperCache()
    }

}
