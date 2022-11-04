package com.myapp.pricing.service.codemapping.cache

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DoNotUseException(currency: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, "Mapping is not allowed for $currency")
