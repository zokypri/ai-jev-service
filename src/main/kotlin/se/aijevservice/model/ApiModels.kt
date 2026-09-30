package se.aijevservice.model

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonProperty

data class CombinedTypesRequest(
    @field:NotEmpty val state: Map<String, Any?>,
    @field:NotEmpty @field:Valid val questions: Map<String, CombinedQuestion>,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CombinedChoiceQuestion::class, name = "choice"),
    JsonSubTypes.Type(value = CombinedNoulQuestion::class, name = "noul"),
    JsonSubTypes.Type(value = CombinedScoreQuestion::class, name = "score"),
)
sealed interface CombinedQuestion {
    val type: String
    val instructions: String
}

data class CombinedChoiceQuestion(
    @field:NotBlank override val instructions: String,
    @field:Size(min = 1, max = 255) val criteria: Map<String, String?>,
    override val type: String = "choice",
) : CombinedQuestion

data class CombinedNoulQuestion(
    @field:NotBlank override val instructions: String,
    @field:Valid val criteria: NoulCriteria,
    override val type: String = "noul",
) : CombinedQuestion

data class CombinedScoreQuestion(
    @field:NotBlank override val instructions: String,
    @field:Size(min = 1) val criteria: List<String>,
    override val type: String = "score",
) : CombinedQuestion

data class ChoiceRequest(
    @field:NotEmpty val state: Map<String, Any?>,
    @field:NotBlank val instructions: String,
    @field:Size(min = 1, max = 255) val criteria: Map<String, String?>,
)

data class NoulRequest(
    @field:NotEmpty val state: Map<String, Any?>,
    @field:NotBlank val instructions: String,
    @field:Valid val criteria: NoulCriteria,
)

data class NoulCriteria(
    @JsonProperty("true") @field:NotBlank val `true`: String,
    @JsonProperty("false") @field:NotBlank val `false`: String,
)

data class ScoreRequest(
    @field:NotEmpty val state: Map<String, Any?>,
    @field:NotBlank val instructions: String,
    @field:Size(min = 1) val criteria: List<String>,
)

data class ChoiceResponse(
    val choice: String,
    val probabilities: Map<String, Double>,
    val confidence: Double,
    val model: String,
    val inputTokens: Int,
    val outputTokens: Int,
) {
    companion object {
        fun from(response: JevChoiceResponse, questionId: String): ChoiceResponse {
            val answer = response.answers[questionId]
                ?: error("Jev response is missing an answer for question '$questionId'")

            return ChoiceResponse(
                choice = answer.choice,
                probabilities = answer.probabilities,
                confidence = answer.confidence,
                model = response.model,
                inputTokens = response.usage.inputTokens,
                outputTokens = response.usage.outputTokens,
            )
        }
    }
}

data class NoulResponse(
    val noul: Double,
    val model: String,
    val inputTokens: Int,
    val outputTokens: Int,
) {
    companion object {
        fun from(response: JevNoulResponse, questionId: String): NoulResponse {
            val answer = response.answers[questionId]
                ?: error("Jev response is missing an answer for question '$questionId'")

            return NoulResponse(
                noul = answer.noul,
                model = response.model,
                inputTokens = response.usage.inputTokens,
                outputTokens = response.usage.outputTokens,
            )
        }
    }
}

data class ScoreResponse(
    val score: Double,
    val probabilities: Map<String, Double>,
    val legend: Map<String, String>,
    val confidence: Double,
    val model: String,
    val inputTokens: Int,
    val outputTokens: Int,
) {
    companion object {
        fun from(response: JevScoreResponse, questionId: String): ScoreResponse {
            val answer = response.answers[questionId]
                ?: error("Jev response is missing an answer for question '$questionId'")

            return ScoreResponse(
                score = answer.score,
                probabilities = answer.probabilities,
                legend = answer.legend,
                confidence = answer.confidence,
                model = response.model,
                inputTokens = response.usage.inputTokens,
                outputTokens = response.usage.outputTokens,
            )
        }
    }
}
