package kr.hhplus.be.server.api.product

import kr.hhplus.be.server.api.product.request.CreateProductRequest
import kr.hhplus.be.server.api.product.response.ProductResponse
import kr.hhplus.be.server.api.product.response.toResponse
import kr.hhplus.be.server.application.product.ProductUseCase
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.command.toCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
  private val productService: ProductService,
  private val productUseCase: ProductUseCase,
) {
  @GetMapping("/{productId}")
  fun get(
    @PathVariable productId: Long,
  ): ProductResponse = productService.get(productId).toResponse()

  @GetMapping
  fun getAll(pageable: Pageable): Page<ProductResponse> =
    productService.getAll(pageable).map { it.toResponse() }

  @PostMapping
  fun create(
    @RequestBody request: CreateProductRequest,
  ): ProductResponse = productService.create(request.toCommand()).toResponse()

  @GetMapping("/top")
  fun getTop(): List<ProductResponse> = productUseCase.top5For3Days().map { it.toResponse() }
}
