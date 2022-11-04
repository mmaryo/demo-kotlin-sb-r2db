package com.myapp.pricing.service.codemapping.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(schema = "common", name = "Entities")
data class Entities(
    @Column("Id") @Id var id: Long? = null,
    @Column("Name") var name: String = "",
    @Column("Code") var code: String = "",
)
