package com.myapp.pricing.service.codemapping.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(schema = "common", name = "Currencies")
data class Currencies(
    @Column("Id") @Id var id: Long? = null,
    @Column("IsoCode") var isoCode: String = "",
)
