package com.myapp.pricing.historical

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication(scanBasePackages = ["com.myapp"])
@EnableWebFlux
@EnableScheduling
class PricingHistoricalApplication

fun main(args: Array<String>) {
    runApplication<PricingHistoricalApplication>(*args)
}
