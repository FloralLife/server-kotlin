package kr.hhplus.be.server.api.product

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/products")
class ProductController {
  class Product(
    val id: Long,
    var name: String,
    var stock: Int,
    val price: Int,
    val createdAt: LocalDateTime,
  )

  val product = Product(1L, "맥북", 50, 2_000_000, LocalDateTime.now())


  @GetMapping("/{productId}")
  fun get(@PathVariable("productId") productId: Long): Product {
    check(productId > 0L) { "상품 id는 양수입니다." }
    require(productId == 1L) { "존재하지 않습니다." }
    return product
  }

  @GetMapping("/top")
  fun getTop() : List<Product> {
    return listOf(
      Product(1L, "맥북 m1", 5, 2_000_000, LocalDateTime.now()),
      Product(2L, "맥북 m2", 2, 2_000_000, LocalDateTime.now()),
      Product(3L, "맥북 m3", 4, 2_000_000, LocalDateTime.now()),
      Product(4L, "맥북 m4", 7, 2_000_000, LocalDateTime.now()),
      Product(5L, "맥북 m5", 4, 2_000_000, LocalDateTime.now())
    )
  }
}
