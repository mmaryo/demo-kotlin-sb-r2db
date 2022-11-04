package com.myapp.pricing.service.codemapping.cache

import com.myappapi.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CurrenciesCodeMapperCacheTest {

    private lateinit var cache: CurrenciesCodeMapperCache

    private val keyId1 = "123456"
    private val keyId2 = "789012"
    private val valueId1 = "eth"
    private val valueId2 = "xrp"

    private val keyKaiko1 = "pdot"
    private val keyKaiko2 = "sspell"
    private val valueKaiko1 = "dot"
    private val valueKaiko2 = "spell"

    private val keyCmc1 = "adda"
    private val valueCmc1 = "ada"

    @BeforeEach
    fun init() {
        val values0 = mutableMapOf<String, String>()
        values0[keyId1] = valueId1
        values0[keyId2] = valueId2

        val values1 = mutableMapOf<String, String>()
        values1[keyKaiko1] = valueKaiko1
        values1[keyKaiko2] = valueKaiko2

        val values2 = mutableMapOf<String, String>()
        values2[keyCmc1] = valueCmc1

        cache = CurrenciesCodeMapperCache()
        cache.set(SourceType.any, values0)
        cache.set(SourceType.kaiko, values1)
        cache.set(SourceType.coinmarketcap, values2)
    }

    @Test
    fun get_should_return_mapped_value() {
        assertThat(cache.get(SourceType.any, keyId1)).isEqualTo(valueId1)
        assertThat(cache.get(SourceType.any, keyId2)).isEqualTo(valueId2)
        assertThat(cache.get(SourceType.kaiko, "$keyKaiko1-usd")).isEqualTo("$valueKaiko1-usd")
        assertThat(cache.get(SourceType.kaiko, "usd-$keyKaiko2")).isEqualTo("usd-$valueKaiko2")
        assertThat(cache.get(SourceType.kaiko, "$keyKaiko1-$keyKaiko2")).isEqualTo("$valueKaiko1-$valueKaiko2")
        assertThat(cache.get(SourceType.coinmarketcap, "$keyCmc1-usd")).isEqualTo("$valueCmc1-usd")
    }

    @Test
    fun get_should_return_key_if_not_mapped() {
        val code = "notexists-notexists"
        assertThat(cache.get(SourceType.any, code)).isEqualTo(code)
        assertThat(cache.get(SourceType.kaiko, code)).isEqualTo(code)
        assertThat(cache.get(SourceType.coinmarketcap, code)).isEqualTo(code)
    }

    @Test
    fun get_should_return_new_value_if_set() {
        // given
        val value = "krak"
        val newValues: MutableMap<String, String> = mutableMapOf()
        newValues[keyKaiko1] = value

        // when
        cache.set(SourceType.kaiko, newValues)

        // then
        assertThat(cache.get(SourceType.kaiko, "$keyKaiko1-usd")).isEqualTo("$value-usd") // changed
        assertThat(cache.get(SourceType.kaiko, "$keyKaiko2-usd")).isEqualTo("$keyKaiko2-usd") // changed because by the set
        assertThat(cache.get(SourceType.coinmarketcap, "$keyKaiko1-usd")).isEqualTo("$keyKaiko1-usd") // not changed
    }

    @Test
    fun get_should_throw_exception_if_mapped_value_is_donotuse() {
        val value = "donotuse"
        val newValues: MutableMap<String, String> = mutableMapOf()
        newValues[keyKaiko1] = value
        cache.set(SourceType.kaiko, newValues)

        assertThatThrownBy { cache.get(SourceType.kaiko, "$keyKaiko1-usd") }.isInstanceOf(DoNotUseException::class.java)
    }

}
