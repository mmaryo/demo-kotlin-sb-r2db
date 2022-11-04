package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.db.entity.Entities
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

/**
 * List all Sources and Providers
 * Contains the ID of the Sources (Kaiko, CoinMarketCap, etc)
 */
interface EntitiesRepository : R2dbcRepository<Entities, Long> {
    fun findByName(name: String): Mono<Entities>
}
