package se.aijevservice.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.aijevservice.model.CombinedTypesRequest
import se.aijevservice.model.ChoiceRequest
import se.aijevservice.model.ChoiceResponse
import se.aijevservice.model.JevCombinedResponse
import se.aijevservice.model.NoulRequest
import se.aijevservice.model.NoulResponse
import se.aijevservice.model.ScoreRequest
import se.aijevservice.model.ScoreResponse
import se.aijevservice.service.JevService

@RestController
@RequestMapping("/api/v1")
class JevController(private val jevService: JevService) {

    @PostMapping("/choices")
    fun choose(@Valid @RequestBody request: ChoiceRequest): ChoiceResponse =
        jevService.choose(request)

    @PostMapping("/nouls")
    fun classify(@Valid @RequestBody request: NoulRequest): NoulResponse =
        jevService.classify(request)

    @PostMapping("/scores")
    fun score(@Valid @RequestBody request: ScoreRequest): ScoreResponse =
        jevService.score(request)

    @PostMapping("/evaluations")
    fun evaluate(@Valid @RequestBody request: CombinedTypesRequest): JevCombinedResponse =
        jevService.evaluate(request)
}
