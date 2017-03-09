// See LICENSE.IBM for license details.

package perfect.util

import chisel3._
import chisel3.util._

class PisoCmd(n: Int) extends Bundle {
  val data = UInt(n.W)
  val count = UInt(log2Up(n).W)
  override def cloneType = new PisoCmd(n).asInstanceOf[this.type]
}

class PisoIO(n: Int) extends Bundle {
  val p = Decoupled(new PisoCmd(n)).flip
  val s = Valid(Bool())
  override def cloneType = new PisoIO(n).asInstanceOf[this.type]
}

class Piso(n: Int) extends Module with UniformPrintfs {
  val io = IO(new PisoIO(n))

  val sr = Reg(UInt(n.W))
  val count = Reg(UInt(log2Up(n).W))

  val sentCount = Reg(UInt(log2Up(n).W))
  val hasData = Reg(init = false.B)
  io.p.ready := !hasData
  when (io.p.fire()) {
    hasData := true.B
    sr := io.p.bits.data
    count := io.p.bits.count
    sentCount := 0.U
  }

  io.s.valid := hasData
  io.s.bits := sr(0)
  when (hasData) {
    sr := sr >> 1
    sentCount := sentCount + 1.U
    hasData := sentCount =/= count
  }

  when (io.p.fire()) {
    printfInfo("Piso: Parallel bits 0x%x, count 0x%x\n", io.p.bits.data, io.p.bits.count)
  }

  assert(!(io.p.valid && !io.p.ready), "Piso: Received parallel data but not ready\n")
}
