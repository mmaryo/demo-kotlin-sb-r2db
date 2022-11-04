package com.myapp.pricing.service.codemapping.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.myappapi.SourceType

class CurrenciesCodeMapperCache {

    var idsCache: Cache<String, String> = buildCache()
    var kaikoCache: Cache<String, String> = buildCache()
    var coinMarketCapCache: Cache<String, String> = buildCache()

    fun set(source: SourceType, newValues: Map<String, String>): CurrenciesCodeMapperCache {
        val newCache = buildCache()
        newCache.putAll(newValues)
        when (source) {
            SourceType.any -> idsCache = newCache
            SourceType.kaiko -> kaikoCache = newCache
            SourceType.coinmarketcap -> coinMarketCapCache = newCache
        }
        return this
    }

    fun get(source: SourceType, asset: String): String {
        return when (source) {
            SourceType.any -> mapCurrency(source, asset)
            SourceType.kaiko -> {
                val splitAsset = splitAsset(asset)
                val base = splitAsset[0]
                val quote = splitAsset[1]
                "${mapCurrency(source, base)}-${mapCurrency(source, quote)}"
            }
            SourceType.coinmarketcap -> {
                val splitAsset = splitAsset(asset)
                val base = splitAsset[0]
                val quote = splitAsset[1]
                "${mapCurrency(source, base)}-${mapCurrency(source, quote)}"
            }
        }
    }

    private fun mapCurrency(source: SourceType, currency: String): String {
        val newCurrency = when (source) {
            SourceType.any -> idsCache.getIfPresent(currency) ?: currency
            SourceType.kaiko -> idsCache.getIfPresent(currency) ?: kaikoCache.getIfPresent(currency) ?: currency
            SourceType.coinmarketcap -> idsCache.getIfPresent(currency) ?: coinMarketCapCache.getIfPresent(currency) ?: currency
        }
        if (newCurrency == "donotuse") {
            throw DoNotUseException(currency)
        }
        return newCurrency
    }

    private fun buildCache(): Cache<String, String> = Caffeine.newBuilder().build()

}

fun splitAsset(asset: String): List<String> {
    val split = asset.split("-")
    if (split.size != 2) {
        throw RuntimeException(asset)
    }
    return split
}
