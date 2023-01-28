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

class PwmIo(n: Int) extends Bundle {
  val enable     = Input(Bool())
  val pulseWidth = Input(UInt(n.W))
  val period     = Input(UInt(n.W))
  val out        = Output(Bool())
}

class Pwm(n: Int) extends Module {
  val io = IO(new PwmIo(n))

  val count = Reg(UInt(n.W))

  io.out := io.enable && count <= io.pulseWidth
  when (io.enable)                        { count := count + 1.U }
  when (count >= io.period || ~io.enable) { count := 0.U         }
}
