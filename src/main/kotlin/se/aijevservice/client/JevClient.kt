package se.aijevservice.client

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import se.aijevservice.model.JevRequest

@Component
class JevClient(private val jevRestClient: RestClient) {

    fun <T : Any> evaluate(request: JevRequest, responseType: Class<T>): T =
        jevRestClient.post()
            .uri(SYSTEM_ONE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(responseType)
            ?: error("Jev returned an empty response body")

    private companion object {
        const val SYSTEM_ONE_PATH = "/v1/systemone"
    }
}
