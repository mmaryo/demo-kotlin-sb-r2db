package com.myapp.pricing.service.codemapping.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(schema = "common", name = "ExchangeCurrencyMappings")
data class ExchangeCurrencyMappings(
    @Column("Id") @Id var id: Long? = null,
    @Column("CryptoExchangeId") var cryptoExchangeId: Long = 0,
    @Column("CurrencyId") var currencyId: Long = 0,
    @Column("Mapping") var mapping: String? = null,
)
