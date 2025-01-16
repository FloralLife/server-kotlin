package kr.hhplus.be.server.infra.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
  private val jpaProductRepository: JpaProductRepository,
) : ProductRepository {
  override fun findAll(pageable: Pageable): Page<Product> = jpaProductRepository.findAll(pageable)

  override fun findById(id: Long): Product? = jpaProductRepository.findByIdOrNull(id)

  override fun findAllForUpdateByIds(ids: List<Long>): List<Product> =
    jpaProductRepository.findAllForUpdateByIdIn(ids)

  override fun findAllByIds(ids: List<Long>): List<Product> = jpaProductRepository.findAllByIdIn(ids)

  override fun save(product: Product): Product = jpaProductRepository.save(product)
}
