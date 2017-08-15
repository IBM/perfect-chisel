// See LICENSE.IBM for license details.

package perfect.random

import chisel3._
import chisel3.util._
import chisel3.testers.BasicTester
import math.pow

class LfsrTester(n: Int, seed: Int = 1) extends BasicTester {
  val dut = Module(new Lfsr(n))
  val count = Reg(init = 0.U((n + 1).W))

  val s_INIT :: s_RUN :: s_CYCLE :: s_DONE :: Nil = Enum(UInt(), 4)
  val state = Reg(UInt(4.W), init = s_INIT)

  dut.io.seed.valid := state === s_INIT
  when (state === s_INIT) {
    dut.io.seed.bits := seed.U
    count := 0.U
    state := s_RUN
  }

  when (state === s_RUN) {
    count := count + 1.U
    when (dut.io.y === seed.U && count > 0.U) {
      state := s_CYCLE
    }
  }

  when (state === s_CYCLE) {
    printf("[INFO] Found period 0d%d\n", count)
    assert(count === math.pow(2, n).toInt.U,
      "LfsrTester: Found unexpectedly short cycle")
    state := s_DONE
  }

  when (state === s_DONE) { stop }

}

class Lfsr8Test extends LfsrTester(8)
class Lfsr16Test extends LfsrTester(16)
