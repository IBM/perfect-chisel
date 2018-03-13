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

trait FletcherH {
  val k_reset = 0
  val k_compute = 1
}

class FletcherCmd(val n: Int) extends Bundle {
  val cmd = UInt(2.W)
  val word = UInt((n/2).W)
}

class FletcherIO(val n: Int) extends Bundle {
  val data = Flipped(Valid(new FletcherCmd(n)))
  val checksum = Output(UInt((n).W))
}

class Fletcher(n: Int) extends Module with FletcherH with UniformPrintfs {
  val io = IO(new FletcherIO(n))

  val a = RegInit(UInt((n/2).W), 0.U)
  val b = RegInit(UInt((n/2).W), 0.U)

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
