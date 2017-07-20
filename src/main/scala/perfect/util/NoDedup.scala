// See LICENSE.IBM for license details.

package perfect
package util

import chisel3._
import chisel3.experimental.ChiselAnnotation
import firrtl.transforms.DedupModules

trait NoDedup {
  self: Module =>

  annotate(ChiselAnnotation(this, classOf[DedupModules], "nodedup!"))
}
