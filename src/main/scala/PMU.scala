package Vivado

import Chisel._
import org.chipsalliance.cde.config.{Config, Field}
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem._
import boom.common.BoomTileAttachParams

case class PMUParams(eventSetSizes: Seq[Int])

case object PMUKey extends Field[Option[PMUParams]](None)

class WithPMU(params: PMUParams) extends Config((site, here, up) => {
  case PMUKey => Some(params)
})

trait CanHavePMU { this: BaseSubsystem =>
  private lazy val nPerfCounters = {
    val tiles = p(TilesLocated(InSubsystem))
    tiles.map {
      case tp: RocketTileAttachParams => tp.tileParams.core.nPerfCounters
      case tp: boom.common.BoomTileAttachParams => tp.tileParams.core.nPerfCounters
      case t => 0
    }.max
  }

  private val device = p(PMUKey).map(params => new DeviceSnippet {
    def describe() = {
      val compat = Seq(ResourceString("riscv,pmu"))
      val events = Seq(
        0x00004 -> BigInt(0x00000302),    // SBI_PMU_HW_CACHE_MISSES
        0x10001 -> BigInt(0x00000202),    // L1D_READ_MISS
        0x10002 -> BigInt(0x00000402),    // L1D_WRITE_ACCESS
        0x10009 -> BigInt(0x00000102),    // L1I_READ_ACCESS
        0x10019 -> BigInt(0x00001002),    // DTLB_READ_MISS
        0x10021 -> BigInt(0x00000802),    // ITLB_READ_MISS
      )

      // TODO Use diplomatic parameters
      val cntrMask = (~(BigInt(-1) << nPerfCounters) << 3) & 0xffffffffL
      // val evtSetSizes = Seq(18, 11, 6, 5, 1)

      Description("pmu", Map(
        "compatible" -> compat,
        "riscv,event-to-mhpmevent" -> events.flatMap { case (evt, sel) =>
          Seq(ResourceInt(evt), ResourceInt(sel >> 32), ResourceInt(sel & 0xffffffffL)) }.toSeq,
        "riscv,event-to-mhpmcounters" -> events.flatMap { case (evt, sel) =>
          Seq(ResourceInt(evt), ResourceInt(evt), ResourceInt(cntrMask)) }.toSeq,
        "riscv,raw-event-to-mhpmcounters" -> params.eventSetSizes.zipWithIndex.flatMap { case (num, i) =>
          val mask = ~(~(BigInt(-1) << num) << 8)
          Seq(ResourceInt(0), ResourceInt(i),
              ResourceInt((mask >> 32) & 0xffffffffL),
              ResourceInt(mask & 0xffffffffL), ResourceInt(cntrMask))
        }
      ))
    }
  })
}

// Taken from Chipyard
class WithNPerfCounters(n: Int = 29) extends Config((site, here, up) => {
  case TilesLocated(InSubsystem) => up(TilesLocated(InSubsystem), site) map {
    case tp: RocketTileAttachParams => tp.copy(tileParams = tp.tileParams.copy(
      core = tp.tileParams.core.copy(nPerfCounters = n)))
    case tp: BoomTileAttachParams => tp.copy(tileParams = tp.tileParams.copy(
      core = tp.tileParams.core.copy(nPerfCounters = n)))
    case other => other
  }
})