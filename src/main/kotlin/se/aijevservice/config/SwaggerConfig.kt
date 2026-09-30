package se.aijevservice.config

import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

private const val CHOICES_PATH = "/api/v1/choices"
private const val NOULS_PATH = "/api/v1/nouls"
private const val SCORES_PATH = "/api/v1/scores"
private const val EVALUATIONS_PATH = "/api/v1/evaluations"

@Configuration
class SwaggerConfig {

    @Bean
    fun jevRequestExampleCustomizer(): OpenApiCustomizer = OpenApiCustomizer { openApi ->
        val examples = mapOf(
            CHOICES_PATH to mapOf(
                "state" to mapOf(
                    "customer_tier" to "enterprise",
                    "ticket" to "Help! My payouts have been failing for 3 days.",
                ),
                "instructions" to "Which team should handle this?",
                "criteria" to mapOf(
                    "billing" to "Payments, invoicing, refunds",
                    "technical" to "Bugs, outages, integrations",
                    "sales" to "Pricing, upgrades, new accounts",
                ),
            ),
            NOULS_PATH to mapOf(
                "state" to mapOf(
                    "customer_tier" to "enterprise",
                    "ticket" to "My checkout page shows a blank screen after I click Pay.",
                ),
                "instructions" to "Is the customer reporting a software defect?",
                "criteria" to mapOf(
                    "true" to "The customer describes broken or unexpected product behavior.",
                    "false" to "The customer is asking a question or requesting a feature.",
                ),
            ),
            SCORES_PATH to mapOf(
                "state" to mapOf(
                    "customer_tier" to "enterprise",
                    "ticket" to "My checkout page shows a blank screen after I click Pay.",
                ),
                "instructions" to "How urgent is this ticket?",
                "criteria" to listOf(
                    "Can wait for the next release",
                    "Should be fixed this week",
                    "Needs immediate attention",
                ),
            ),
            EVALUATIONS_PATH to mapOf(
                "state" to mapOf(
                    "customer_tier" to "enterprise",
                    "ticket" to "My checkout page shows a blank screen after I click Pay.",
                ),
                "questions" to mapOf(
                    "is_bug" to mapOf(
                        "type" to "noul",
                        "instructions" to "Is the customer reporting a software defect?",
                        "criteria" to mapOf(
                            "true" to "The customer describes broken or unexpected product behavior.",
                            "false" to "The customer is asking a question or requesting a feature.",
                        ),
                    ),
                    "team" to mapOf(
                        "type" to "choice",
                        "instructions" to "Which team should own this ticket?",
                        "criteria" to mapOf(
                            "payments" to "Checkout, billing, or payment processing issues.",
                            "frontend" to "Rendering, layout, or browser compatibility issues.",
                            "account" to "Login, permissions, or profile issues.",
                        ),
                    ),
                    "urgency" to mapOf(
                        "type" to "score",
                        "instructions" to "How urgent is this ticket?",
                        "criteria" to listOf(
                            "Can wait for the next release",
                            "Should be fixed this week",
                            "Needs immediate attention",
                        ),
                    ),
                ),
            ),
        )

        examples.forEach { (path, example) ->
            openApi.paths
                ?.get(path)
                ?.post
                ?.requestBody
                ?.content
                ?.get(MediaType.APPLICATION_JSON_VALUE)
                ?.example = example
        }
    }
}
