// See LICENSE.ibm for license details.
package perfect.util

import chisel3._
import config._
import fox.Debug

trait UniformPrintfs {
  val p: Parameters

  def printfPrefix(prefix: String, message: String, args: Bits*): Unit = {
    if (p(Debug)) { printf(prefix + message, args:_*) }}

  def printfInfo (m: String, a: Bits*) { printfPrefix("[INFO] ",  m, a:_*) }
  def printfWarn (m: String, a: Bits*) { printfPrefix("[WARN] ",  m, a:_*) }
  def printfError(m: String, a: Bits*) { printfPrefix("[ERROR] ", m, a:_*) }
  def printfDebug(m: String, a: Bits*) { printfPrefix("[DEBUG] ", m, a:_*) }
  def printfTodo (m: String, a: Bits*) { printfPrefix("[TODO] ",  m, a:_*) }
}
