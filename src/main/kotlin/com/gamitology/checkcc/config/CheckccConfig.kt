package com.gamitology.checkcc.config

//@ConfigurationProperties(prefix = "checkcc")
data class CheckccConfig(
    var apiUrl: String = "",
    var symbol: String = "",
    var label: String = "",
    var format: String = ""
)