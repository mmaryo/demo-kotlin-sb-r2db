package com.myapp.pricing.historical.config

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UnauthorizedException : ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
