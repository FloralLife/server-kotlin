package kr.hhplus.be.server.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(val code: String, val message: String)

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(IllegalArgumentException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun handleException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
    logger.info(e.message)
    return ResponseEntity(
      ErrorResponse("404", "${e.message}"),
      HttpStatus.NOT_FOUND,
    )
  }

  @ExceptionHandler(IllegalStateException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleException(e: IllegalStateException): ResponseEntity<ErrorResponse> {
    logger.info(e.message)
    return ResponseEntity(
      ErrorResponse("400", "${e.message}"),
      HttpStatus.BAD_REQUEST,
    )
  }


  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
    logger.error(e.message)
    return ResponseEntity(
      ErrorResponse("500", "${e.message}"),
      HttpStatus.INTERNAL_SERVER_ERROR,
    )
  }
}
