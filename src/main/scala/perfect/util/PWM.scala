// See LICENSE for license details
package perfect.util

import chisel3._

class PwmIo(n: Int) extends Bundle {
  val enable     = Input(Bool())
  val pulseWidth = Input(UInt(n.W))
  val period     = Input(UInt(n.W))
  val out        = Output(Bool())
  override def cloneType: this.type = new PwmIo(n).asInstanceOf[this.type]
}

class Pwm(n: Int) extends Module {
  val io = IO(new PwmIo(n))

  val count = Reg(UInt(n.W))

  io.out := io.enable && count <= io.pulseWidth
  when (io.enable)                        { count := count + 1.U }
  when (count >= io.period || ~io.enable) { count := 0.U         }
}
