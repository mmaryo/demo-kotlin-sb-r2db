package com.myapp.pricing.historical.config

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*


@ConditionalOnProperty(prefix = "filter.tracing", name = ["enable"], havingValue = "true", matchIfMissing = true)
@Configuration
class TracingFilterConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun traceIdFilter(): TracingFilter {
        log.info("-- Start Request Tracing Filter")
        return TracingFilter()
    }

}

@Order(-1)
class TracingFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).contextWrite { context ->

            val traceIdName = "TraceId"
            val traceId: String = exchange.request.headers.getFirst(traceIdName)
                ?: UUID.randomUUID().toString()
            MDC.put(traceIdName, traceId)
            context.put(traceIdName, traceId)
            exchange.response.headers.add(traceIdName, traceId)

            val sourceNameName = "SourceName"
            val sourceName: String? = exchange.request.headers.getFirst(sourceNameName)
            if (!sourceName.isNullOrBlank()) {
                MDC.put(sourceNameName, sourceName)
                context.put(sourceNameName, sourceName)
                exchange.response.headers.add(sourceNameName, sourceName)
            }

            val objectNameName = "ObjectName"
            val objectName: String? = exchange.request.headers.getFirst(objectNameName)
            if (!objectName.isNullOrBlank()) {
                MDC.put(objectNameName, objectName)
                context.put(objectNameName, objectName)
                exchange.response.headers.add(objectNameName, objectName)
            }

            val objectIdName = "ObjectId"
            val objectId: String? = exchange.request.headers.getFirst(objectIdName)
            if (!objectId.isNullOrBlank()) {
                MDC.put(objectIdName, objectId)
                context.put(objectIdName, objectId)
                exchange.response.headers.add(objectIdName, objectId)
            }

            context
        }
    }
}
