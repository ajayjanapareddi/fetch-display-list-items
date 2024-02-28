package com.example.fetchdisplaylistitems.repository

import com.example.fetchdisplaylistitems.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class ItemRepository {

    private val baseUrl = "https://fetch-hiring.s3.amazonaws.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    // return items as flow
    fun getItems(): Flow<List<Item>> = flow {
        try {
            val items = apiService.getItems()
            emit(items)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // error case
        }
    }

//    // loads the given json data from assets directory
//    private fun loadJsonData(): String {
//        return try {
//            context.assets.open("items.json").bufferedReader().use { it.readText() }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            ""
//        }
//    }

//    @OptIn(ExperimentalSerializationApi::class)
//    private fun processItems(jsonData: String): List<Item> {
//        // Converts JSON string to a list of Items and returns them
//        return json.decodeFromString<List<Item>>(jsonData)
//    }
}

interface ApiService {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}
