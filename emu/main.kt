class IO16x8(
    val addr: i16 = i16(),
    val data: i8 = i8()
) {
    fun read(readAddr: i16): i8 {
        addr(readAddr)
        return data()
    }
}

class APUCore(val trigger: i16) {
    suspend fun process() {
        while (trigger()) {}
    }
}

class APU(io: IO16x8) {
    val apuCore = APUCore()

    fun process() {
        val data = when(io.addr) {
            0x100 -> apuCore.trigger(1.f)
        }
    }
}

fun forever(label: String, guard: () -> Unit, action: () -> Unit) {
    println(label)
    guard()
    action()
}


class PPU (
    io: IO16x8,
    interrupt: Interrupt
)
{
    val float: display

    fun process() {
        val STAT = 0.i8
        val LY = 0.i8
        val LYC = 0.i8

        forever("io") {
            when(io.addr()) {
                0xFF45 -> when {
                    io.dataIn.ready -> io.dataOut(LY)
                    default         -> {}
                }
                0xFF45 -> when {
                    io.dataIn.ready  -> LYC(io.dataIn)
                    default          -> io.dataOut(LYC)
                }
            }
        }

        forever("interrupt",{ LY() == LYC }) {
            interrupt(1)
        }

        forever("oam",{ STAT[1 downTo 0 ]() == 2 }) {
            /* oam */
        }

        forever("bgTile",{ STAT[1 downTo 0 ]() == 2 && bgIndex = 0 }) {
            /* bgtile */
        }

        forever("draw",{ STAT[1 downTo 0 ]() == 3 }) {
            /* draw */
        }

        forever("raster") {
            for (line in 0 .. 143) {
                /* oam */
                LY(line)
                STAT[1 downTo 0] = 2
                for (dot in 0 .. 100) {}

                /* draw */
                for (dot in 0 .. 160) {
                    STAT[1 downTo 0] = 3
                }

                /* hblank */
                STAT[1 downTo 0] = 0
                for (dot in 260 .. 456) {}

            }

            /* vblank */
            STAT[1 downTo 0] = 1
            for (line in 144 .. 153) {
                LY(line)
            }
        }
    }
}

class ALU(
    val op: Int,
    val a: Int,
    val b: Int
) {
    val result: Int
    val flags: Int

    init {
        result = a + b
        flags = 0
    }
}


class CPU {
    val io: IO16x8

    val PC = 0.i16
    fun process() {
        val inst = io.read(PC)
    }
}

class GB {
    val cpu = CPU()
    val APU = PPU(apu.io)
}