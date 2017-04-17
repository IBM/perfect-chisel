// See LICENSE.IBM for license details.

package perfect.util

import chisel3._
import chisel3.util._

trait FletcherH {
  val k_reset = 0
  val k_compute = 1
}

class FletcherCmd(n: Int) extends Bundle {
  val cmd = UInt(2.W)
  val word = UInt((n/2).W)
  override def cloneType: this.type = new FletcherCmd(n).asInstanceOf[this.type]
}

class FletcherIO(n: Int) extends Bundle {
  val data = Flipped(Valid(new FletcherCmd(n)))
  val checksum = Output(UInt((n).W))
}

class Fletcher(n: Int) extends Module with FletcherH with UniformPrintfs {
  val io = IO(new FletcherIO(n))

  val a = Reg(UInt((n/2).W), init = 0.U)
  val b = Reg(UInt((n/2).W), init = 0.U)

  val do_reset = io.data.fire() && io.data.bits.cmd === k_reset.U
  val do_compute = io.data.fire() && io.data.bits.cmd === k_compute.U

  io.checksum := b ## a

  when (do_reset) {
    a := 0.U
    b := 0.U
    printfInfo("Fletcher: Reset\n")
  }

  when (do_compute) {
    val new_a = a + io.data.bits.word
    a := new_a
    b := b + new_a
    printfInfo("Fletcher: Compute a => 0x%x, b => 0x%x\n", new_a, b + new_a)
  }
}
