// See LICENSE for license details.

package perfect

import chisel3._
import chisel3.util._

class CounterWithReset(n: Int) extends Counter(n: Int) {
  def reset(): Unit = { value := 0.U }
}

class CounterModule(n: Int) extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val extReset = Input(Bool())
    val max = Input(UInt(log2Up(n + 1).W))
    val wrap = Output(Bool())
    val value = Output(UInt(log2Up(n).W))
  })

  val value = Reg(init = 0.U(n.W))
  io.value := value
  when (io.inc) { value := value + 1.U }
  io.wrap := value === (io.max - 1.U)
  when (io.extReset || io.wrap) { value := 0.U }
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

  def apply(cond: Bool, reset: Bool, max: UInt): (UInt, Bool) = {
    val c = Module(new CounterModule(max.getWidth))
    c.io.inc := cond
    c.io.extReset := reset
    c.io.max := max
    val Seq(value, wrap) = Seq(UInt(), Bool())
    (c.io.value, cond && c.io.wrap)
  }
}
