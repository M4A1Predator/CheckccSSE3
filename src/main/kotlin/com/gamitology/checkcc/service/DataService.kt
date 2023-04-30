package com.gamitology.checkcc.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.gamitology.checkcc.config.CheckccConfig
import com.gamitology.checkcc.model.PriceData
import com.gamitology.checkcc.util.getStringParams
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Component
class DataService(
    private val objectMapper: ObjectMapper,
    private val checkccConfig: CheckccConfig,
    private val ssE3Service: SSE3Service
) {
    val df: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var lastCheckTime: Long? = null
    var textLine1 = "-"
    var textLine2 = "-"

    fun process() {
        try {
            ssE3Service.init()
            pollPrice()
            ssE3Service.sendText(this.textLine1, this.textLine2)
        } catch (ex: Exception) {
            return
        }
    }

    private fun pollPrice() {
        val now = Date()
        if (lastCheckTime != null) {
            val diffMS = abs(now.time - lastCheckTime!!)
            val diff = TimeUnit.MINUTES.convert(diffMS, TimeUnit.MILLISECONDS)
            if (diff <= 15) {
                return
            }
        }

        val priceData = getPrice(checkccConfig.symbol)
        if (priceData != null) {
            this.textLine1 = "At " + getTimeText(priceData.timestamp)
            this.textLine2 = checkccConfig.label + " " + priceData.price
        }
        lastCheckTime = now.time
    }

    private fun getPrice(symbol: String): PriceData? {
        val params = HashMap<String, String>()
        params["symbol"] = symbol
        params["limit"] = "1"

        val url = URL(checkccConfig.apiUrl + "?" + getStringParams(params))
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.doOutput = true
        con.useCaches = false

        var data = ""
        BufferedReader(InputStreamReader(con.inputStream)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                data += line
            }
        }

        val json = objectMapper.readTree(data)

        if (json.size() == 0) {
            return null
        }

        return PriceData(
            formatPrice(json.get(0).get("price").toString()),
            json.get(0).get("time").asLong()
        )
    }

    fun formatPrice(priceStr: String): String {
        val f = DecimalFormat(checkccConfig.format)
        val price = priceStr.replace("\"", "").toDouble()
        return f.format(price)
    }

    private fun getTimeText(inputMs : Long): String {
        val d : LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(inputMs), ZoneId.systemDefault())
//		val d2 = d.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Bangkok")).toLocalDateTime()
        return df.format(d)
    }
}
