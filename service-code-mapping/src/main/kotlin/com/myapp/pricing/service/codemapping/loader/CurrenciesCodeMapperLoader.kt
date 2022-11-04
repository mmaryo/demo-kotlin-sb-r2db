package com.myapp.pricing.service.codemapping.loader

import com.myapp.pricing.service.codemapping.db.entity.Currencies
import com.myapp.pricing.service.codemapping.db.entity.ExchangeCurrencyMappings
import com.myapp.pricing.service.codemapping.db.repository.CurrenciesRepository
import com.myapp.pricing.service.codemapping.db.repository.ExchangeCurrencyMappingsRepository
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Load currencies mapping for a source
 */
class CurrenciesCodeMapperLoader(
    private val currenciesRepository: CurrenciesRepository,
    private val exchangeCurrencyMappingsRepository: ExchangeCurrencyMappingsRepository,
) {
    fun load(): Mono<MutableMap<String, String>> {
        return currenciesRepository.findAll()
            .reduce(mutableMapOf()) { results: MutableMap<String, String>, currency: Currencies ->
                results[currency.id.toString()] = format(currency.isoCode)
                results
            }
    }

    fun load(sourcesId: Long): Mono<MutableMap<String, String>> {
        return currenciesRepository.findAll().collectList()
            .flatMap { currencies ->
                val i = AtomicInteger()
                exchangeCurrencyMappingsRepository.findByCryptoExchangeId(sourcesId)
                    .filter { it.mapping != null }
                    .doOnNext { i.getAndIncrement() }
                    .reduce(mutableMapOf()) { results: MutableMap<String, String>, code: ExchangeCurrencyMappings ->
                        results[inputCode(currencies, code)] = format(code.mapping!!)
                        results
                    }
            }
    }

    private fun inputCode(
        currencies: List<Currencies>,
        mapCode: ExchangeCurrencyMappings
    ) = currencies.find { it.id == mapCode.currencyId }!!.isoCode.lowercase(Locale.getDefault())

    private fun format(code: String) =
        code.lowercase(Locale.getDefault())
}
