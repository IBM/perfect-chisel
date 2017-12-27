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
