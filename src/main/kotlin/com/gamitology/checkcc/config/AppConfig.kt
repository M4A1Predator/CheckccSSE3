package com.gamitology.checkcc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class AppConfig {

    @Bean
    fun checkccConfig(): CheckccConfig {
        return CheckccConfig()
    }

}