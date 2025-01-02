package kr.hhplus.be.server.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/products")
class ProductController {
  class Product(
    val id: Long,
    var name: String,
    var stock: Int,
    val price: Int,
    val createdAt: LocalDateTime,
  )

  val product = Product(1L, "맥북", 5, 2_000_000, LocalDateTime.now())


  @GetMapping("/{productId}")
  fun get(@PathVariable("productId") productId: Long): Product {
    require(productId > 0L) { "상품 id는 양수입니다." }
    require(productId == 1L) { "존재하지 않습니다." }
    return product
  }
}
