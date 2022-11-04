package com.myapp.pricing.service.codemapping

import com.myapp.pricing.service.codemapping.db.entity.CryptoExchanges
import com.myapp.pricing.service.codemapping.db.repository.CryptoExchangesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class MyService {

    @Autowired
    lateinit var cryptoExchangesRepository: CryptoExchangesRepository

    fun load(): Flux<CryptoExchanges> {
        return cryptoExchangesRepository.findAll()
    }

}
