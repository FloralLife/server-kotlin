package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.command.CreateProductCommand
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
  val productRepository: ProductRepository,
) {
  fun get(id: Long): Product =
    productRepository.findById(id)
      ?: throw NotFoundException(id, Product::class.java)

  fun getAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

  fun getAllWithLock(ids: List<Long>): List<Product> {
    val products = productRepository.findAllForUpdateByIds(ids)
    validateAllExists(ids, products)
    return products
  }

  fun getAll(ids: List<Long>): List<Product> {
    val products = productRepository.findAllByIds(ids)
    validateAllExists(ids, products)
    return products
  }

  fun create(command: CreateProductCommand): Product =
    productRepository.save(
      Product(
        name = command.name,
        price = command.price,
        stock = command.stock,
      ),
    )

  fun validateAllExists(ids: List<Long>, products: List<Product>) {
    val foundProductIds = products.map { it.id }.toSet()
    val notExistProductIds = ids - foundProductIds
    if (notExistProductIds.isNotEmpty()) {
      throw NotFoundException("상품이 존재하지 않습니다.: $notExistProductIds")
    }
  }
}
