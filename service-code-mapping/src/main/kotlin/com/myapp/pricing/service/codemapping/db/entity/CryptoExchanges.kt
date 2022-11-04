package com.myapp.pricing.service.codemapping.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(schema = "common", name = "CryptoExchanges")
data class CryptoExchanges(
    @Column("Id") @Id var id: Long? = null,
    @Column("KaikoExchangeCode") var kaikoExchangeCode: String? = null,
)
