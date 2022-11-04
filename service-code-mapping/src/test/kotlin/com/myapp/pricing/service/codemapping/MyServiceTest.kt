package com.myapp.pricing.service.codemapping

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MyServiceTest {

    @Autowired
    lateinit var myService: com.myapp.pricing.service.codemapping.MyService

    @Test
    fun load() {
        println(myService.load().collectList().block())
    }
}
