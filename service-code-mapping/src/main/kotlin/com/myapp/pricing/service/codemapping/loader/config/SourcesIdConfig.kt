package com.myapp.pricing.service.codemapping.loader.config

import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import com.myapp.pricing.service.codemapping.loader.SourcesId
import com.myapp.pricing.service.codemapping.loader.SourcesIdLoader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Get sources ID from code name
 */
@Configuration
open class SourcesIdConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var entitiesRepository: EntitiesRepository

    private val sourcesIdLoader by lazy { SourcesIdLoader(entitiesRepository) }

    companion object {
        internal const val sourceKaikoCode = "Kaiko"
        internal const val sourceCoinMarketCapCode = "CoinMarketCap"

    }

    @Bean
    @ConditionalOnProperty(value = ["sources-ids.enable"], havingValue = "true", matchIfMissing = true)
    open fun initSourcesId(): SourcesId {
        log.info("-- Load SourcesId")
        return sourcesIdLoader.sourcesId()
            .block()!!
    }

    @Bean
    @ConditionalOnProperty(value = ["sources-ids.enable"], havingValue = "false")
    open fun emptySourceId(): SourcesId {
        log.warn("-- Empty SourcesId")
        return SourcesId(-1L, -1L)
    }

}

