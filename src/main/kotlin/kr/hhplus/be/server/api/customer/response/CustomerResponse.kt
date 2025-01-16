package kr.hhplus.be.server.api.customer.response

import kr.hhplus.be.server.domain.customer.Customer

data class CustomerResponse(
  val id: Long,
  val point: Int,
)

fun Customer.toResponse(): CustomerResponse =
  CustomerResponse(
    id = this.id,
    point = this.point,
  )
