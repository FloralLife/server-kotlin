package kr.hhplus.be.server.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findAll(pageable: Pageable): Page<Product>

    fun findById(id: Long): Product?

    fun findAllForUpdateByIds(ids: List<Long>): List<Product>

    fun findAllByIds(ids: List<Long>): List<Product>

    fun save(product: Product): Product
}
