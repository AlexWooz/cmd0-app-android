package com.cmd0app.cmd0AppAndroid.dbOps

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.io.FileInputStream
import java.util.Properties


class DBOps {

    val prop = Properties()
    val input = FileInputStream("config.properties")
    //get mongoUser property from config.properties and store it in a variable
    val mongoUser = prop.getProperty("mongoUser")
    //get mongoPassword property from config.properties and store it in a variable
    val mongoPassword = prop.getProperty("mongoPassword")

    fun getDB():MongoDatabase {
        val connectionString =
                ConnectionString(String.format("mongodb+srv://%s:%s@cluster0.yy0jlun.mongodb.net/?retryWrites=true&w=majority", mongoUser, mongoPassword))
        val settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(
                        ServerApi.builder()
                                .version(ServerApiVersion.V1)
                                .build()
                )
                .build()
        val mongoClient = MongoClients.create(settings)
        return mongoClient.getDatabase("sample_mflix")

    }

    fun getCollection(collection_name:String):MongoCollection<Document> {
        val database = getDB()
        return database.getCollection(collection_name)
    }

    fun findDocument(query:String):Document {
        val collection = getCollection("movies")
        return collection.find(Document.parse(query)).first()

    }

    fun findManyDocuments(query:String):List<Document> {
        val collection = getCollection("movies")
        return collection.find(Document.parse(query)).toList()
    }
}