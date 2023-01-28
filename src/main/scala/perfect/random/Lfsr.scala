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
package perfect.random

import chisel3._
import chisel3.util._

object LfsrTaps {
  // This was sourced from:
  // \@TechReport{,
  // author = {R. W. Ward and T.C.A. Molteno},
  // title = {Table of Linear Feedback Shift Registers},
  // institution = {University of Otago},
  // year = {2012},
  // number = {2012-1},
  // issn = {1172-496X}
  // }
  def apply(size: Int): Seq[Int] = { // scalastyle:off magic.number
    size match {
      case 2    => Seq(1)
      case 3    => Seq(2)
      case 4    => Seq(3)
      case 5    => Seq(3)
      case 6    => Seq(5)
      case 7    => Seq(6)
      case 8    => Seq(6, 5, 4)
      case 9    => Seq(5)
      case 10   => Seq(7)
      case 11   => Seq(9)
      case 12   => Seq(11, 8, 6)
      case 13   => Seq(12, 10, 9)
      case 14   => Seq(13, 11, 9)
      case 15   => Seq(14)
      case 16   => Seq(14, 13, 11)
      case 17   => Seq(14)
      case 18   => Seq(11)
      case 19   => Seq(18, 17, 14)
      case 20   => Seq(17)
      case 21   => Seq(19)
      case 22   => Seq(21)
      case 23   => Seq(18)
      case 24   => Seq(23, 21, 20)
      case 25   => Seq(25, 24, 20)
      case 27   => Seq(26, 25, 22)
      case 28   => Seq(25)
      case 29   => Seq(27)
      case 30   => Seq(29, 26, 24)
      case 31   => Seq(28)
      case 32   => Seq(30, 26, 25)
      case 64   => Seq(63, 61, 60)
      case 128  => Seq(127, 126, 121)
      case 256  => Seq(254, 251, 246)
      case 512  => Seq(510, 507, 504)
      case 1024 => Seq(1015, 1002, 1001)
      case 2048 => Seq(2035, 2034, 2029)
      case 4096 => Seq(4095, 4081, 4069) // scalastyle:on magic.number
      case _    => throw new Exception("No LFSR taps stored for requested size")
    }
  }
}

class Lfsr(n: Int, seed: Int = 1) extends Module {
  require(seed > 0, "Seed cannot be zero")

  val io = IO(new PrngIo(n))

  val shiftReg = RegInit(VecInit((seed.U(n.W)).asBools))
  shiftReg.zipWithIndex.map {case (x, i) => { x := shiftReg((i + 1) % n) } }
  LfsrTaps(n) map (x => { shiftReg(x - 1) := shiftReg(0) ^ shiftReg(x) })

  when (io.seed.fire()) {
    shiftReg zip io.seed.bits.asBools map { case(l, r) => l := r } }
  io.y := shiftReg.asUInt
}

class Lfsr8 extends Lfsr(8)
class Lfsr16 extends Lfsr(16)
class Lfsr64 extends Lfsr(64)
