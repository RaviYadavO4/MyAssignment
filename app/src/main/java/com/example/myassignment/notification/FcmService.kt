package com.example.myassignment.notification

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import java.net.HttpURLConnection
import java.net.URL

class FcmService(
    private val context: Context,
    private val serviceAccountFileName: String,  // e.g., "your-service-account.json"
    private val projectId: String
) {

    fun sendNotificationToToken(token: String, title: String, message: String) {
        val serviceAccountStream = context.assets.open(serviceAccountFileName)

        val credentials = GoogleCredentials.fromStream(serviceAccountStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        credentials.refreshIfExpired()

        val accessToken = credentials.accessToken.tokenValue

        val jsonPayload = """
            {
              "message": {
                "token": "$token",
                "notification": {
                  "title": "$title",
                  "body": "$message"
                }
              }
            }
        """.trimIndent()

        val url = URL("https://fcm.googleapis.com/v1/projects/$projectId/messages:send")
        val connection = url.openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Authorization", "Bearer $accessToken")
            setRequestProperty("Content-Type", "application/json; UTF-8")
        }

        connection.outputStream.use {
            it.write(jsonPayload.toByteArray())
        }

        val response = connection.inputStream.bufferedReader().readText()
        println("FCM Response: $response")
    }
}


