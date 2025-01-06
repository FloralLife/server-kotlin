package kr.hhplus.be.server.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.BaseEntity

@Entity
@Table(name = "product")
class Product(
  val name: String,

  var stock: Int,

  var price: Int,
) : BaseEntity()
