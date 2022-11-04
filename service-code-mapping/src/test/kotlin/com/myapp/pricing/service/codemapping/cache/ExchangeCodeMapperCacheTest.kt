package com.myapp.pricing.service.codemapping.cache

import com.myappapi.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExchangeCodeMapperCacheTest {

    private lateinit var cache: ExchangeCodeMapperCache

    private val key1 = "1"
    private val key2 = "2"
    private val key3 = "3"
    private val key4 = "4"
    private val value1 = "binc"
    private val value2 = "okex"
    private val value3 = "ftxx"
    private val value4 = "kucoin"

    @BeforeEach
    fun init() {
        val values1 = mutableMapOf<String, String>()
        values1[key1] = value1
        values1[key2] = value2
        values1[key3] = value3

        val values2 = mutableMapOf<String, String>()
        values2[key4] = value4

        cache = ExchangeCodeMapperCache()
        cache.set(SourceType.kaiko, values1)
        cache.set(SourceType.coinmarketcap, values2)
    }

    @Test
    fun get_should_return_mapped_value() {
        assertThat(cache.get(SourceType.kaiko, key1)).isEqualTo(value1)
        assertThat(cache.get(SourceType.kaiko, key2)).isEqualTo(value2)
        assertThat(cache.get(SourceType.kaiko, key3)).isEqualTo(value3)
        assertThat(cache.get(SourceType.coinmarketcap, key4)).isEqualTo(value4)
    }

    @Test
    fun get_should_return_key_if_not_mapped() {
        val code = "notexists"
        assertThatThrownBy { cache.get(SourceType.any, code) }.isInstanceOf(UnsupportedOperationException::class.java)
        assertThat(cache.get(SourceType.kaiko, code)).isEqualTo(code)
        assertThat(cache.get(SourceType.coinmarketcap, code)).isEqualTo(code)
    }

    @Test
    fun get_should_return_new_value_if_set() {
        // given
        val value = "krak"
        val newValues: MutableMap<String, String> = mutableMapOf()
        newValues[key1] = value

        // when
        cache.set(SourceType.kaiko, newValues)

        // then
        assertThat(cache.get(SourceType.kaiko, key1)).isEqualTo(value) // changed
        assertThat(cache.get(SourceType.kaiko, key2)).isEqualTo(key2) // not changed
        assertThat(cache.get(SourceType.kaiko, key3)).isEqualTo(key3) // not changed
        assertThat(cache.get(SourceType.coinmarketcap, key4)).isEqualTo(value4) // not changed
    }

    @Test
    fun get_should_throw_exception_if_mapped_value_is_donotuse() {
        val value = "donotuse"
        val newValues: MutableMap<String, String> = mutableMapOf()
        newValues[key1] = value
        cache.set(SourceType.kaiko, newValues)

        assertThatThrownBy { cache.get(SourceType.kaiko, key1) }.isInstanceOf(DoNotUseException::class.java)
    }

}
