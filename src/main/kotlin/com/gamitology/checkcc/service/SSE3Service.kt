package com.gamitology.checkcc.service

import com.gamitology.checkcc.util.createPostReq
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.io.*
import java.nio.file.Files

@Component
class SSE3Service(
    private val objectMapper: ObjectMapper
) {

    companion object {
        val GAMENAME_VAL: String = "CHECKCC"
        val GAMENAME_KEY: String = "game"
        val EVENT_VAL: String = "HEALTH"
        val EVENT_KEY: String = "event"
    }

    private var address = ""
    var initiated = false
    var valueNum = 1

    fun init() {
        if (initiated) {
            return
        }
        println("Initiating...")
        val address = findAddress()

        removeEvent(address)
        registerEvent(address)
        this.initiated = true
        println("Initiated")
    }

    private fun removeEvent(address: String) {
        val dataMap = HashMap<String, String>()
        dataMap[GAMENAME_KEY] = GAMENAME_VAL
        dataMap[EVENT_KEY] = EVENT_VAL
        val postData = objectMapper.writeValueAsString(dataMap)

        val conn = createPostReq("http://$address/remove_game_event", postData)
        var resData = ""
        try {
            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    resData += line
                }
            }
        } catch (ex: IOException) {
            BufferedReader(InputStreamReader(conn.errorStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    resData += line
                }
            }
        }
        println("REMOVE EVENT RESULT : $resData")
    }

    private fun registerEvent(address: String) {
        println("Registering Event")
        val cpr = ClassPathResource("event_template.json")
        val reader = BufferedReader(InputStreamReader(cpr.inputStream))
        var template = reader.lines().reduce { acc, l -> acc + l }.get()

        template = template.replace("%GAMENAME%", GAMENAME_VAL)
        template = template.replace("%EVENTNAME%", EVENT_VAL)

        val conn = createPostReq("http://$address/bind_game_event", template)
        var resData = ""
        try {
            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    resData += line
                }
            }
            println("BIND EVENT RESULT : $resData")
        } catch (ex: IOException) {
            println(ex.message)
            BufferedReader(InputStreamReader(conn.errorStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    resData += line
                }
            }
            println("BIND EVENT ERROR : $resData")
            throw Exception("Bind Event Failed")
        } catch (ex : Exception) {
            println(ex.message)
        }
    }

    private fun findAddress(): String {
        if (address.isNotEmpty()) {
            return address
        }

        var jsonAddressStr = ""
        val corePropsFileName = if (System.getProperty("os.name").startsWith("Windows")) {
            System.getenv("PROGRAMDATA") +
                    "\\SteelSeries\\SteelSeries Engine 3\\coreProps.json";
        } else {
            // Mac path to coreProps.json
            "/Library/Application Support/" +
                    "SteelSeries Engine 3/coreProps.json";
        }

        try {
            val coreProps = BufferedReader(FileReader(corePropsFileName))
            jsonAddressStr = coreProps.readLine()
            println("Opened coreProps.json and read: $jsonAddressStr")
            coreProps.close()
        } catch (e: FileNotFoundException) {
            println("coreProps.json not found")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Unhandled exception.")
        }

        val json = objectMapper.readTree(jsonAddressStr)
        address = json.get("address").asText()
        return address
    }

    fun sendText(text1: String, text2: String) {
        val frameMap = HashMap<String, String>()
        frameMap["first"] = text1
        frameMap["second"] = text2

        val eventDataMap = HashMap<String, Any>()
        if (valueNum > 100) {
            valueNum = 1
        }
        eventDataMap["value"] = (valueNum++).toString()
        eventDataMap["frame"] = frameMap

        val dataMap = HashMap<String, Any>()
        dataMap[GAMENAME_KEY] = GAMENAME_VAL
        dataMap[EVENT_KEY] = EVENT_VAL
        dataMap["data"] = eventDataMap

        val postData = objectMapper.writeValueAsString(dataMap)
//        println(postData)

        val address = findAddress()

        val conn = createPostReq("http://$address/game_event", postData)
        val statusCode = conn.responseCode
        if (!statusCode.toString().startsWith("2")) {
            println("Can't send game event")
        }
    }
}