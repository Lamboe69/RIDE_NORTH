package com.ridenorth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RideNorthApplication

fun main(args: Array<String>) {
    runApplication<RideNorthApplication>(*args)
}
