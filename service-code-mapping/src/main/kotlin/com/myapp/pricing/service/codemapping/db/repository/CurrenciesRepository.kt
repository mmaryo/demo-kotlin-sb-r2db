package com.myapp.pricing.service.codemapping.db.repository

import com.myapp.pricing.service.codemapping.db.entity.Currencies
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface CurrenciesRepository : R2dbcRepository<Currencies, Long> {
    fun findByIsoCode(isoCode: String): Mono<Currencies>
}
