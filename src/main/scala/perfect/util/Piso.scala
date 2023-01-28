// Copyright 2017 IBM
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package perfect.util

import chisel3._
import chisel3.util._

class PisoCmd(n: Int) extends Bundle {
  val data = UInt(n.W)
  val count = UInt(log2Ceil(n).W)
}

class PisoIO(n: Int) extends Bundle {
  val p = Flipped(Decoupled(new PisoCmd(n)))
  val s = Valid(Bool())
}

class Piso(n: Int) extends Module with UniformPrintfs {
  val io = IO(new PisoIO(n))

  val sr = Reg(UInt(n.W))
  val count = Reg(UInt(log2Ceil(n).W))

  val sentCount = Reg(UInt(log2Ceil(n).W))
  val hasData = RegInit(false.B)
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
    printfInfo("Piso: Parallel bits 0x%x, count 0x%x\n", io.p.bits.data,
      io.p.bits.count)
  }

  assert(!(io.p.valid && !io.p.ready),
    "Piso: Received parallel data but not ready\n")
}
