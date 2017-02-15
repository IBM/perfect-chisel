// See LICENSE.ibm for license details.
package perfect.util

import chisel3._
import config._

trait UniformPrintfs {
  def printfPrefix(prefix: String, message: String, args: Bits*): Unit = {
    printf(prefix + message, args:_*) }

  val printfSigil = ""

  def printfInfo (m: String, a: Bits*) { printfPrefix("[INFO] ",  printfSigil++m, a:_*) }
  def printfWarn (m: String, a: Bits*) { printfPrefix("[WARN] ",  printfSigil++m, a:_*) }
  def printfError(m: String, a: Bits*) { printfPrefix("[ERROR] ", printfSigil++m, a:_*) }
  def printfDebug(m: String, a: Bits*) { printfPrefix("[DEBUG] ", printfSigil++m, a:_*) }
  def printfTodo (m: String, a: Bits*) { printfPrefix("[TODO] ",  printfSigil++m, a:_*) }
}
