package com.example.tripwise.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class CurrencyConverter {
    private val apiKey = "4c252a808e79615033308511"
    private val baseUrl = "https://v6.exchangerate-api.com/v6/$apiKey"

    suspend fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        return withContext(Dispatchers.IO) {
            val url = "$baseUrl/pair/$fromCurrency/$toCurrency/$amount"
            val response = URL(url).readText()
            val jsonObject = JSONObject(response)
            jsonObject.getDouble("conversion_result")
        }
    }
}