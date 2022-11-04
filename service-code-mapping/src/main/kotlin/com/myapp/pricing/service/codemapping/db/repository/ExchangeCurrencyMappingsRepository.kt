package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.db.entity.ExchangeCurrencyMappings
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

/**
 * Provide mapping for an exchange code for each Entity
 */
interface ExchangeCurrencyMappingsRepository : R2dbcRepository<ExchangeCurrencyMappings, Long> {
    fun findByCryptoExchangeId(cryptoExchangeId: Long): Flux<ExchangeCurrencyMappings>

}
