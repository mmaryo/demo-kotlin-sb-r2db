package com.myapp.pricing.service.codemapping

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext
import org.springframework.data.relational.core.mapping.NamingStrategy
import java.util.*


@Configuration
open class R2dbcConfiguration {
    @Primary
    @Bean
    open fun r2dbcMappingContext(
        namingStrategy: Optional<NamingStrategy>,
        r2dbcCustomConversions: R2dbcCustomConversions
    ): R2dbcMappingContext {
        val context = R2dbcMappingContext(NamingStrategy.INSTANCE)
        context.setSimpleTypeHolder(r2dbcCustomConversions.simpleTypeHolder)
        context.isForceQuote = true
        return context
    }
}
