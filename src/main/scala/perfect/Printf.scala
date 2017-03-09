// See LICENSE.IBM for license details.

package perfect.util

import chisel3._
import config._

trait UniformPrintfs {
  val printfSigil = ""

  def pp(prefix: String, message: String, args: Bits*): Unit = {
    printf(prefix + message, args:_*) }

  def printfInfo (m: String, a: Bits*) { pp("[INFO] ",  printfSigil++m, a:_*) }
  def printfWarn (m: String, a: Bits*) { pp("[WARN] ",  printfSigil++m, a:_*) }
  def printfError(m: String, a: Bits*) { pp("[ERROR] ", printfSigil++m, a:_*) }
  def printfDebug(m: String, a: Bits*) { pp("[DEBUG] ", printfSigil++m, a:_*) }
  def printfTodo (m: String, a: Bits*) { pp("[TODO] ",  printfSigil++m, a:_*) }
}
