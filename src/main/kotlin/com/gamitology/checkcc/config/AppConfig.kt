package com.gamitology.checkcc.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File
import java.nio.file.Paths

@Configuration
@EnableScheduling
class AppConfig {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Bean
    fun checkccConfig(): CheckccConfig {
        val configFile = File(Paths.get(System.getProperty("user.dir"), "conf.json").toString())
        return objectMapper.readValue(configFile)
    }

}