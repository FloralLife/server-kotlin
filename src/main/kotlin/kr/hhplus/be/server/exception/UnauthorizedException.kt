package kr.hhplus.be.server.exception

class UnauthorizedException : RuntimeException {
  constructor(message: String) : super(message)

  constructor(message: String, cause: Throwable) : super(message, cause)
}
