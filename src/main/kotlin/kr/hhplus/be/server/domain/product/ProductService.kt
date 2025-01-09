package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
  val productRepository: ProductRepository,
) {
  fun get(id: Long): Product {
    return productRepository.findById(id) ?: throw HhpNotFoundException(id, Product::class.java)
  }

  fun getAll(pageable: Pageable): Page<Product> {
    return productRepository.findAll(pageable)
  }

  fun getAllWithLock(ids: List<Long>): List<Product> {
    return productRepository.findAllForUpdateByIds(ids)
  }

  fun getAll(ids: List<Long>): List<Product> {
    return productRepository.findAllByIds(ids)
  }

  fun create(command: CreateProductCommand): Product {
    return productRepository.save(
      Product(
        name = command.name,
        price = command.price,
        stock = command.stock,
      )
    )
  }
}
