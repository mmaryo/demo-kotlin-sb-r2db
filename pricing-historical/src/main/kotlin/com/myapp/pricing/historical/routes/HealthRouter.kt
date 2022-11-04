package com.myapp.pricing.historical.routes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Component
class HealthRouter {
    @Bean("healthBean")
    fun route(@Autowired handler: HealthHandler) = router {
        GET("/health2").invoke(handler::health)
    }
}

@Component
class HealthHandler {

    fun health(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue("ok")
    }

}
