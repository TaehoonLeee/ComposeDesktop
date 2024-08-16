package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

object ApiExecutor {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val client: HttpClient = HttpClient(CIO) {
        defaultRequest {
            header("Accept", "application/json")
            header("Authorization", "Referer TBD")
            header("Content-type", "application/json")

            url {
                protocol = URLProtocol.HTTPS
                host = ""
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun fetch(phase: String = "dev"): DistributionList {
        val response = client.get<HttpStatement>("") {
            parameter("phase", phase)
            parameter("format", "json")
        }.execute()

        if (response.status.isSuccess().not()) {
            throw ServerError(response.status.description)
        }

        return response.receive()
    }
}