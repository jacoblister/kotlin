class IO16x8(
    val addr: i16,
    val dataIn: i8
) {
    val dataOut: i8

//    fun read(readAddr: i16): i8 {
//        addr(readAddr)
//        return dataIn()
//    }
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

class PPU (
    io: IO16x8,
    interrupt: Interrupt
)
{
    val float: display

    suspend fun process() {
        val LY = 0.i16
        val LYC = 0.i16

        forever("io") {
            when(io.addr()) {
                0xFF45 -> when(io.dataIn.signaled) {
                    false -> io.dataOut(LY)
                    true -> {}
                }
                0xFF45 -> when(io.w) {
                    true  -> LYC(io.dataIn)
                    false -> io.dataOut(LYC)
                }
            }
        }

        forever("interrupt") {
            if (LYC() == LY) {
                interrupt(1)
            }
        }

        forever("raster") {
            for (line in 0 .. 143) {
                LY(line)
                while (0) { /* oam */ }
                while (0) { /* draw */ }
                while (0) { /* hblank */ }
            }
            for (line in 144 .. 153) {
                /* vblank */
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