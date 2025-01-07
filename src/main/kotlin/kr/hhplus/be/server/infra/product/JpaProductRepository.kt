package kr.hhplus.be.server.infra.product

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock


interface JpaProductRepository : JpaRepository<Product, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  fun findAllForUpdateByIdIn(ids: List<Long>): List<Product>

}
