package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {
    @Mock
    lateinit var customerRepository: CustomerRepository

    @InjectMocks
    lateinit var customerService: CustomerService

    private lateinit var customer: Customer

    @BeforeEach
    fun setUp() {
        customer = Customer(randomId(), 10_000)
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회시 NotFoundException 발생")
    fun getUserThenNotFound() {
        whenever(customerRepository.findById(anyLong())).thenReturn(null)

        assertThrows(NotFoundException::class.java) { customerService.get(randomId()) }
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회시 NotFoundException 발생")
    fun getUserForUpdateThenNotFound() {
        whenever(customerRepository.findForUpdateById(anyLong())).thenReturn(null)

        assertThrows(NotFoundException::class.java) { customerService.getWithLock(randomId()) }
    }

    @Test
    @DisplayName("존재하지 않는 유저의 포인트를 충전하려고 할 때 NotFoundException 발생")
    fun chargePointForNotExistCustomerThenNotFound() {
        whenever(customerRepository.findForUpdateById(anyLong())).thenReturn(null)

        assertThrows(NotFoundException::class.java) { customerService.chargePoint(randomId(), 100) }
    }

    @Test
    @DisplayName("포인트를 한도 이상으로 충전하려고 하면 IllegalArgumentException 발생")
    fun chargePointMoreThanMaxThenIllegalArgumentException() {
        whenever(customerRepository.findForUpdateById(customer.id)).thenReturn(customer)

        assertThrows(IllegalArgumentException::class.java) {
            customerService.chargePoint(customer.id, Int.MAX_VALUE)
        }
    }

    @Test
    @DisplayName("포인트 충전 후 유저의 잔액 증가")
    fun chargePoint() {
        val before = customer.point
        val amount = 10_000

        whenever(customerRepository.findForUpdateById(customer.id)).thenReturn(customer)

        customerService.chargePoint(customer.id, amount)

        assertEquals(before + amount, customer.point)
    }

    @Test
    @DisplayName("존재하지 않는 유저의 포인트를 사용하려고 할 때 NotFoundException 발생")
    fun usePointForNotExistCustomerThenNotFound() {
        whenever(customerRepository.findForUpdateById(anyLong())).thenReturn(null)

        assertThrows(NotFoundException::class.java) { customerService.usePoint(randomId(), 100) }
    }

    @Test
    @DisplayName("포인트를 잔액 이상으로 사용하려고 하면 IllegalArgumentException 발생")
    fun usePointMoreThanMaxThenIllegalArgumentException() {
        whenever(customerRepository.findForUpdateById(customer.id)).thenReturn(customer)

        assertThrows(IllegalArgumentException::class.java) {
            customerService.usePoint(customer.id, Int.MAX_VALUE)
        }
    }

    @Test
    @DisplayName("포인트 사용 후 유저의 잔액 감소")
    fun usePoint() {
        val before = customer.point
        val amount = 5_000

        whenever(customerRepository.findForUpdateById(customer.id)).thenReturn(customer)

        customerService.usePoint(customer.id, amount)

        assertEquals(before - amount, customer.point)
    }
}
