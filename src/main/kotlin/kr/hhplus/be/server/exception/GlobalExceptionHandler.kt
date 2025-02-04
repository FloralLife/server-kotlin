package kr.hhplus.be.server.exception

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(
  val code: String,
  val message: String,
)

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(IllegalArgumentException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
    logger.info(e.message)
    return ResponseEntity(
      ErrorResponse("400", "${e.message}"),
      HttpStatus.BAD_REQUEST,
    )
  }

  @ExceptionHandler(UnauthorizedException::class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  fun handleException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
    logger.info(e.message)
    return ResponseEntity(
      ErrorResponse("401", "${e.message}"),
      HttpStatus.UNAUTHORIZED,
    )
  }

  @ExceptionHandler(NotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun handleNotFound(e: NotFoundException): ResponseEntity<ErrorResponse> {
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

  @ExceptionHandler(DataIntegrityViolationException::class)
  fun handleException(e: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
    val error = e.cause
    if (error is ConstraintViolationException) {
      if (error.kind == ConstraintViolationException.ConstraintKind.UNIQUE) {
        logger.info(e.message)
        return ResponseEntity(
          ErrorResponse("409", "중복된 키 값 입니다."),
          HttpStatus.CONFLICT,
        )
      }
    }
    logger.error(e.message)
    return ResponseEntity(
      ErrorResponse("500", "${e.message}"),
      HttpStatus.INTERNAL_SERVER_ERROR,
    )
  }

  @ExceptionHandler(Exception::class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
    logger.error(e.message, e)
    return ResponseEntity(
      ErrorResponse("500", "${e.message}"),
      HttpStatus.INTERNAL_SERVER_ERROR,
    )
  }
}
