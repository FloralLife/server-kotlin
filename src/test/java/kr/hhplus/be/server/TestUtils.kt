package kr.hhplus.be.server

import kotlin.random.Random

object TestUtils {
  fun randomId(): Long = Random.nextLong(1, 1_000_000)
}
