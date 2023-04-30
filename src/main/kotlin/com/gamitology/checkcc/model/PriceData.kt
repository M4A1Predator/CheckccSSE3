package com.gamitology.checkcc.model

data class PriceData(
    val price: String,
    val timestamp: Long
) {

    constructor() : this("", 0)

}