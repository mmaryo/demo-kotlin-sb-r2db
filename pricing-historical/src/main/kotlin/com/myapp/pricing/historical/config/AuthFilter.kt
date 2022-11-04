package com.myapp.pricing.historical.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Configuration
@Order(-2)
class AuthFilterConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("#{'\${security.keys}'.split(',')}")
    lateinit var keys: List<String>

    @Value("\${spring.profiles.active}")
    lateinit var env: String

    @Bean
    fun authFilter(): AuthFilter {
        log.info("-- Start Auth Filter with ${keys.size} api keys")
        return AuthFilter(keys, env)
    }

}

open class AuthFilter(private val keys: List<String>, private val env: String) : WebFilter {

    private val prod = "production"

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).contextWrite { context ->
            // exclude health-check and info routes
            val path = exchange.request.path.value()
            if (path != "/health" && path != "/info" && path != "/prometheus") {

                val authorizationHeader: String? = exchange.request.headers.getFirst("X-Api-Key")
                val authorizationQueryParam: String? = exchange.request.queryParams.getFirst("key")
                val key: String

                // check the header
                if (authorizationHeader != null) {
                    if (authorizationHeader.isBlank()) {
                        throw UnauthorizedException()
                    }
                    key = authorizationHeader

                }
                // check the query param
                else if (authorizationQueryParam != null && !env.equals(prod, true)) {
                    // query param key can be use in all envs but not in prod
                    key = authorizationQueryParam

                }
                // if no key: error
                else {
                    throw UnauthorizedException()
                }

                // check if the key is valid
                if (!keys.contains(key)) {
                    throw UnauthorizedException()
                }

            }

            context
        }
    }
}
