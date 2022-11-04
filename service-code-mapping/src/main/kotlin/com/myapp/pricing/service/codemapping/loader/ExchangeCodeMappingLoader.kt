package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.db.entity.CryptoExchanges
import com.myapp.pricing.service.codemapping.db.entity.Entities
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import com.myapp.pricing.service.codemapping.db.repository.EntitiesRepository
import reactor.core.publisher.Mono


/**
 * Load mapping code for exchanges for Kaiko
 */
class ExchangeCodeMappingLoader(
    private var cryptoExchangesRepository: CryptoExchangesRepository,
    private var entitiesRepository: EntitiesRepository,
) {
    fun loadIdsToKaiko(): Mono<MutableMap<String, String>> {
        return cryptoExchangesRepository
            .findByKaikoExchangeCodeNotNull()
            .collectList()
            .flatMap { ce: MutableList<CryptoExchanges> ->
                entitiesRepository
                    .findAllById(ce.map { it.id })
                    .reduce(mutableMapOf()) { results: MutableMap<String, String>, ex: Entities ->
                        results[ex.id!!.toString()] = ce.find { it.id == ex.id }!!.kaikoExchangeCode!!
                        results
                    }
            }
    }

    fun loadCodeToKaiko(): Mono<MutableMap<String, String>> {
        return cryptoExchangesRepository
            .findByKaikoExchangeCodeNotNull()
            .collectList()
            .flatMap { ce: MutableList<CryptoExchanges> ->
                entitiesRepository
                    .findAllById(ce.map { it.id })
                    .reduce(mutableMapOf()) { results: MutableMap<String, String>, ex: Entities ->
                        results[formatExchangeName(ex)] = ce.find { it.id == ex.id }!!.kaikoExchangeCode!!
                        results
                    }
            }
    }

    private fun formatExchangeName(ex: Entities) = ex.code.replace("[^a-zA-Z0-9]+", "").lowercase()

}
