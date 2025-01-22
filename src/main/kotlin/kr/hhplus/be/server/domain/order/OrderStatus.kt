package kr.hhplus.be.server.domain.order

enum class OrderStatus {
  PAYMENT_WAITING,
  PAYMENT_COMPLETED,
  PRODUCT_PREPARING,
  MOVING,
  DELIVERED,
  CANCELLED,
}
