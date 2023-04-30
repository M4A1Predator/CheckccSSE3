package com.gamitology.checkcc.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "checkcc")
data class CheckccConfig(
    var apiUrl: String = "",
    var symbol: String = "",
    var label: String = "",
    var format: String = ""
)