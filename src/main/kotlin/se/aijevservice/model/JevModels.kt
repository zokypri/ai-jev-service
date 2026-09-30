package se.aijevservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class JevRequest(
    val state: Map<String, Any?>,
    val model: String,
    val questions: Map<String, JevQuestion>,
)

sealed interface JevQuestion {
    val instructions: String
    val type: String
}

data class JevChoiceQuestion(
    override val instructions: String,
    val criteria: Map<String, String?>,
    override val type: String = "choice",
) : JevQuestion

data class JevNoulQuestion(
    override val instructions: String,
    val criteria: Map<String, String>,
    override val type: String = "noul",
) : JevQuestion

data class JevScoreQuestion(
    override val instructions: String,
    val criteria: List<String>,
    override val type: String = "score",
) : JevQuestion

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevChoiceResponse(
    val model: String,
    val answers: Map<String, JevChoiceAnswer>,
    val usage: JevUsage,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevChoiceAnswer(
    val type: String,
    val choice: String,
    val probabilities: Map<String, Double>,
    val confidence: Double,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevNoulResponse(
    val model: String,
    val answers: Map<String, JevNoulAnswer>,
    val usage: JevUsage,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevNoulAnswer(
    val type: String,
    val noul: Double,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevScoreResponse(
    val model: String,
    val answers: Map<String, JevScoreAnswer>,
    val usage: JevUsage,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevCombinedResponse(
    val model: String,
    val answers: Map<String, JevCombinedAnswer>,
    val usage: JevUsage,
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class JevCombinedAnswer(
    val type: String,
    val choice: String? = null,
    val noul: Double? = null,
    val score: Double? = null,
    val probabilities: Map<String, Double>? = null,
    val legend: Map<String, String>? = null,
    val confidence: Double? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevScoreAnswer(
    val type: String,
    val score: Double,
    val probabilities: Map<String, Double>,
    val legend: Map<String, String>,
    val confidence: Double,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JevUsage(
    @JsonProperty("input_tokens") val inputTokens: Int,
    @JsonProperty("output_tokens") val outputTokens: Int,
)
