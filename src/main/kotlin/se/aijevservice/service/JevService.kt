package se.aijevservice.service

import org.springframework.stereotype.Service
import se.aijevservice.client.JevClient
import se.aijevservice.config.JevProperties
import se.aijevservice.model.CombinedChoiceQuestion
import se.aijevservice.model.CombinedNoulQuestion
import se.aijevservice.model.CombinedScoreQuestion
import se.aijevservice.model.CombinedTypesRequest
import se.aijevservice.model.ChoiceRequest
import se.aijevservice.model.ChoiceResponse
import se.aijevservice.model.JevChoiceResponse
import se.aijevservice.model.JevChoiceQuestion
import se.aijevservice.model.JevCombinedResponse
import se.aijevservice.model.JevQuestion
import se.aijevservice.model.JevNoulQuestion
import se.aijevservice.model.JevNoulResponse
import se.aijevservice.model.JevRequest
import se.aijevservice.model.JevScoreQuestion
import se.aijevservice.model.JevScoreResponse
import se.aijevservice.model.NoulRequest
import se.aijevservice.model.NoulResponse
import se.aijevservice.model.ScoreRequest
import se.aijevservice.model.ScoreResponse

@Service
class JevService(
    private val jevClient: JevClient,
    private val jevProperties: JevProperties,
) {

    fun choose(request: ChoiceRequest): ChoiceResponse {
        val jevRequest = JevRequest(
            state = request.state,
            model = jevProperties.model,
            questions = mapOf(
                CHOICE_QUESTION_ID to JevChoiceQuestion(
                    instructions = request.instructions,
                    criteria = request.criteria,
                ),
            ),
        )
        val response = jevClient.evaluate(jevRequest, JevChoiceResponse::class.java)
        return ChoiceResponse.from(response, CHOICE_QUESTION_ID)
    }

    fun classify(request: NoulRequest): NoulResponse {
        val jevRequest = JevRequest(
            state = request.state,
            model = jevProperties.model,
            questions = mapOf(
                NOUL_QUESTION_ID to JevNoulQuestion(
                    instructions = request.instructions,
                    criteria = mapOf(
                        "true" to request.criteria.`true`,
                        "false" to request.criteria.`false`,
                    ),
                ),
            ),
        )
        val response = jevClient.evaluate(jevRequest, JevNoulResponse::class.java)
        return NoulResponse.from(response, NOUL_QUESTION_ID)
    }

    fun score(request: ScoreRequest): ScoreResponse {
        val jevRequest = JevRequest(
            state = request.state,
            model = jevProperties.model,
            questions = mapOf(
                SCORE_QUESTION_ID to JevScoreQuestion(
                    instructions = request.instructions,
                    criteria = request.criteria,
                ),
            ),
        )
        val response = jevClient.evaluate(jevRequest, JevScoreResponse::class.java)
        return ScoreResponse.from(response, SCORE_QUESTION_ID)
    }

    fun evaluate(request: CombinedTypesRequest): JevCombinedResponse {
        val jevRequest = JevRequest(
            state = request.state,
            model = jevProperties.model,
            questions = request.questions.mapValues { (_, question) -> question.toJevQuestion() },
        )
        return jevClient.evaluate(jevRequest, JevCombinedResponse::class.java)
    }

    private fun se.aijevservice.model.CombinedQuestion.toJevQuestion(): JevQuestion =
        when (this) {
            is CombinedChoiceQuestion -> JevChoiceQuestion(instructions, criteria)
            is CombinedNoulQuestion -> JevNoulQuestion(
                instructions = instructions,
                criteria = mapOf(
                    "true" to criteria.`true`,
                    "false" to criteria.`false`,
                ),
            )
            is CombinedScoreQuestion -> JevScoreQuestion(instructions, criteria)
        }

    private companion object {
        const val CHOICE_QUESTION_ID = "choice"
        const val NOUL_QUESTION_ID = "noul"
        const val SCORE_QUESTION_ID = "score"
    }
}
