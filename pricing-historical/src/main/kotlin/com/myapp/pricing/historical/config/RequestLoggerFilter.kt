package com.myapp.pricing.historical.config

import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.util.StopWatch
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets


@ConditionalOnProperty(prefix = "filter.request-logger", name = ["enable"], havingValue = "true", matchIfMissing = true)
@Configuration
class RequestLoggerFilterConfig {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun requestLoggerFilter(): RequestLoggerFilter {
        log.info("-- Start Request Log Filter")
        return RequestLoggerFilter()
    }

}

class RequestLoggerFilter : WebFilter {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        return if (!exchange.request.uri.path.contains("/health") && log.isInfoEnabled) {

            val sw = StopWatch()
            sw.start()
            val bodyCaptureExchange = BodyCaptureExchange(exchange)
            chain.filter(bodyCaptureExchange).doOnSuccess {

                val request = exchange.request
                val response = exchange.response

                val method = request.method
                val path = request.uri.path

                val statusCode = httpStatus(response)
                sw.stop()
                val time = "${sw.totalTimeMillis}ms"

                val bodyRequest = bodyCaptureExchange.request.fullBody
                val bodyResponse = bodyCaptureExchange.response.fullBody

                val message = "(${time}) " +
                        ">>> $method $path $bodyRequest " +
                        "<<< ${statusCode.value()} $bodyResponse"

                MDC.put("executionTime", time)
                MDC.put("httpMethod", method.name())
                MDC.put("httpPath", path)
                MDC.put("httpStatus", statusCode.value().toString())
                MDC.put("bodyRequest", bodyRequest)
                MDC.put("bodyResponse", bodyResponse)

                if (bodyResponse.contains("error")) {
                    log.warn(message)
                } else {
                    log.info(message)
                }

                MDC.put("RequestTime", time)
            }

        } else {
            chain.filter(exchange)
        }
    }

    private fun httpStatus(response: ServerHttpResponse) = try {
        response.statusCode!!
    } catch (ex: Exception) {
        HttpStatus.CONTINUE
    }

}

class BodyCaptureExchange(exchange: ServerWebExchange) : ServerWebExchangeDecorator(exchange) {

    private val bodyCaptureRequest: BodyCaptureRequest
    private val bodyCaptureResponse: BodyCaptureResponse

    override fun getRequest(): BodyCaptureRequest {
        return bodyCaptureRequest
    }

    override fun getResponse(): BodyCaptureResponse {
        return bodyCaptureResponse
    }

    init {
        bodyCaptureRequest = BodyCaptureRequest(exchange.request)
        bodyCaptureResponse = BodyCaptureResponse(exchange.response)
    }
}

class BodyCaptureRequest internal constructor(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {

    private val body = StringBuilder()

    override fun getBody(): Flux<DataBuffer> {
        return super.getBody().doOnNext { buffer: DataBuffer ->
            capture(
                buffer
            )
        }
    }

    private fun capture(buffer: DataBuffer) {
        body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString())
    }

    val fullBody: String
        get() = body.toString().replace("[\n\r]", "").replace("\\s".toRegex(), "")
}

class BodyCaptureResponse internal constructor(delegate: ServerHttpResponse) : ServerHttpResponseDecorator(delegate) {

    private val body = StringBuilder()

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(Flux.from(body).doOnNext { buffer: DataBuffer ->
            capture(
                buffer
            )
        })
    }

    private fun capture(buffer: DataBuffer) {
        body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString())
    }

    val fullBody: String
        get() = body.toString().replace("[\n\r]", "").replace("\\s".toRegex(), "")
}
