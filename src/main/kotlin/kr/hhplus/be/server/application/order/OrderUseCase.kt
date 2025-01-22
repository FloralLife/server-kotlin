package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.application.order.command.CreateOrderUCCommand
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.customer.CustomerCouponService
import kr.hhplus.be.server.domain.customer.CustomerService
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.command.CreateOrderCommand
import kr.hhplus.be.server.domain.order.command.CreateOrderProductCommand
import kr.hhplus.be.server.domain.order.model.OrderResult
import kr.hhplus.be.server.domain.order.model.toResult
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderUseCase(
  private val customerService: CustomerService,
  private val customerCouponService: CustomerCouponService,
  private val couponService: CouponService,
  private val productService: ProductService,
  private val orderService: OrderService,
) {
  @Transactional
  fun order(command: CreateOrderUCCommand): OrderResult {
    val customer = customerService.getWithLock(command.customerId)

    val (customerCoupon, discountRate) =
      command.customerCouponId?.let {
        customerCouponService.get(it).let { coupon ->
          coupon to couponService.get(coupon.coupon.id).discountRate
        }
      } ?: (null to 0)

    customerCoupon?.use()

    val products = productService.getAllWithLock(command.products.map { it.productId })
    val orderProducts = products.associateBy { it.id }
    val orderProductCommands =
      command.products.map { CreateOrderProductCommand(orderProducts[it.productId]!!, it.amount) }

    val order =
      orderService.create(
        CreateOrderCommand(
          address = command.address,
          customer = customer,
          customerCoupon = customerCoupon,
          discountRate = discountRate,
          products = orderProductCommands,
        ),
      )

    order.products.forEach { it.product.purchase(it.amount) }

    return order.toResult()
  }
}
