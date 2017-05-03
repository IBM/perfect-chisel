// See LICENSE.IBM for license details.

package perfect

import chisel3._
import chisel3.util._

class CounterWithReset(n: Int) extends Counter(n: Int) {
  def reset(): Unit = { value := 0.U }
}

object CounterWithReset {
  def apply(n: Int): CounterWithReset = new CounterWithReset(n)

  def apply(cond: Bool, reset: Bool, n: Int): (UInt, Bool) = {
    val c = new CounterWithReset(n)
    var wrap: Bool = null
    when (cond) { wrap = c.inc() }
    when (reset) { c.reset() }
    (c.value, cond && wrap)
  }
}
