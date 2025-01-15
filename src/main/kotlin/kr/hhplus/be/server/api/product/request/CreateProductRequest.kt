package kr.hhplus.be.server.api.product.request

data class CreateProductRequest(
    val name: String,
    val stock: Int,
    val price: Int,
)
