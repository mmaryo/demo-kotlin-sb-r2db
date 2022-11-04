package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.db.entity.CryptoExchanges
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux


/**
 * Where to find the Kaiko Exchange Code
 */
interface CryptoExchangesRepository : R2dbcRepository<CryptoExchanges, Long> {
    fun findByKaikoExchangeCodeNotNull(): Flux<CryptoExchanges>

}
