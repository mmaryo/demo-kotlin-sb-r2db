package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import reactor.core.publisher.Mono

/**
 * Get sources ID from code name
 */
open class SourcesIdLoader(
    private var entitiesRepository: EntitiesRepository
) {

    companion object {
        internal const val sourceKaikoCode = "Kaiko"
        internal const val sourceCoinMarketCapCode = "CoinMarketCap"
    }

    fun sourcesId(): Mono<SourcesId> {
        return Mono.zip(
            entitiesRepository.findByName(sourceKaikoCode),
            entitiesRepository.findByName(sourceCoinMarketCapCode)
        )
            .map {
                SourcesId(
                    it.t1.id!!,
                    it.t2.id!!,
                )
            }
    }

}

data class SourcesId(
    val sourceKaikoId: Long,
    val sourceCoinMarketCapId: Long,
)

