package se.aijevservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestClientResponseException

@RestControllerAdvice
class JevExceptionHandler {

    // Surfaces Jev's own status and error body so it is easy to see what Jev rejected
    @ExceptionHandler(RestClientResponseException::class)
    fun handleJevError(ex: RestClientResponseException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_GATEWAY,
            "Jev responded with ${ex.statusCode.value()}: ${ex.responseBodyAsString}",
        )
}
