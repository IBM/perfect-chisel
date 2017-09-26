// See LICENSE for license details.
package perfect.random

import chisel3._
import chisel3.util._

class PrngIo(n: Int) extends Bundle {
  val seed = Input(Valid(UInt(n.W)))
  val y = Output(UInt(n.W))
  override def cloneType: this.type = new PrngIo(n).asInstanceOf[this.type]
}
