package com.gamitology.checkcc.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@Configuration
class JacksonConfig {

//    @Bean
//    @Primary
//    fun objectMapper(): ObjectMapper {
////        return ObjectMapper()
////            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return jacksonMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL).build()
    }

}