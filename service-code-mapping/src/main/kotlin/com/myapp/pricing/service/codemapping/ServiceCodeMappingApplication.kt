package com.myapp.pricing.service.codemapping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@EnableR2dbcRepositories
open class ServiceCodeMappingApplication

fun main(args: Array<String>) {
    runApplication<ServiceCodeMappingApplication>(*args)
}
