package com.myapp.pricing.service.codemapping.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.myappapi.SourceType

class ExchangeCodeMapperCache {

    var kaikoCache: Cache<String, String> = buildCache()
    var cmcCache: Cache<String, String> = buildCache()

    internal fun set(source: SourceType, newValues: Map<String, String>): ExchangeCodeMapperCache {
        val newCache = buildCache()
        newCache.putAll(newValues)
        when (source) {
            SourceType.any -> throw UnsupportedOperationException()
            SourceType.kaiko -> kaikoCache = newCache
            SourceType.coinmarketcap -> cmcCache = newCache
        }
        return this
    }

    fun getOrNot(source: SourceType, code: String?): String? {
        return if (code.isNullOrEmpty()) code else get(source, code)
    }

    fun get(source: SourceType, exchange: String): String {
        val newExchange = when (source) {
            SourceType.any -> throw UnsupportedOperationException()
            SourceType.kaiko -> kaikoCache.getIfPresent(exchange) ?: exchange
            SourceType.coinmarketcap -> cmcCache.getIfPresent(exchange) ?: exchange
        }
        if (newExchange == "donotuse") {
            throw DoNotUseException(exchange)
        }
        return newExchange
    }

    private fun buildCache(): Cache<String, String> = Caffeine.newBuilder().build()
}
