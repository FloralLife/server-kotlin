package kr.hhplus.be.server.domain.product.command

import kr.hhplus.be.server.api.product.request.CreateProductRequest

data class CreateProductCommand(
  val name: String,
  val stock: Int,
  val price: Int,
)

fun CreateProductRequest.toCommand() = CreateProductCommand(name, stock, price)
