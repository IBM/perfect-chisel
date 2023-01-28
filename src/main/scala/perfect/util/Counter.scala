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
package perfect

import chisel3._
import chisel3.util._

class CounterWithReset(n: Int) extends Counter(n: Int) {
  override def reset(): Unit = { value := 0.U }
}

class CounterModule(n: Int) extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val extReset = Input(Bool())
    val max = Input(UInt(log2Ceil(n + 1).W))
    val wrap = Output(Bool())
    val value = Output(UInt(log2Ceil(n).W))
  })

  val value = RegInit(0.U(n.W))
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
