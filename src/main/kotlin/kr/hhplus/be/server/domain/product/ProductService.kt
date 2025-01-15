package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.product.command.CreateProductCommand
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository,
) {
    fun get(id: Long): Product = productRepository.findById(id) ?: throw HhpNotFoundException(id, Product::class.java)

    fun getAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

    fun getAllWithLock(ids: List<Long>): List<Product> = productRepository.findAllForUpdateByIds(ids)

    fun getAll(ids: List<Long>): List<Product> = productRepository.findAllByIds(ids)

    fun create(command: CreateProductCommand): Product =
        productRepository.save(
            Product(
                name = command.name,
                price = command.price,
                stock = command.stock,
            ),
        )
}
