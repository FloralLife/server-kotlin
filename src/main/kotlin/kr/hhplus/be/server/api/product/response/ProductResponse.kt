package kr.hhplus.be.server.api.product.response

import kr.hhplus.be.server.domain.product.Product
import java.time.LocalDateTime

data class ProductResponse(
  val id: Long,
  val name: String,
  val stock: Int,
  val price: Int,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)

fun Product.toResponse() =
  ProductResponse(
    id,
    name,
    stock,
    price,
    createdAt,
    updatedAt,
  )
