package com.myappapi

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
enum class ApiType {
    pair,
    perpetualfuture,
    future,
    option,

    // Kaiko specific
    direct,
    spot,
}
