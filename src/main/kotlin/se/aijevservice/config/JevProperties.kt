package se.aijevservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "jev")
data class JevProperties(
    val baseUrl: String,
    val apiKey: String,
    val model: String,
    val connectTimeout: Duration,
    val readTimeout: Duration,
)
