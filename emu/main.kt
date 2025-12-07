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
            0x100.i8 -> apuCore.trigger(1.f)
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

    init {
        val STAT = 0.i8
        val LY = 0.i8
        val LYC = 0.i8

        val oam = Register(40) {
            class OAM(val x: i8 = 0.i8, val y: i8 = 0.i8, val attr: i8 = 0.i8)
            OAM()
        }

        forever("io") {
            when(io.addr()) {
                0xFF45.i16 -> when {
                    io.data.ready -> LY(io.data)
                    default       -> {}
                }
                0xFF45.i16 -> when {
                    io.data.ready  -> LYC(io.dataIn)
                    default        -> io.data(LYC)
                }
            }
        }

        forever("interrupt",{ LY() == LYC }) {
            interrupt(1)
        }

        forever("raster") {
            for (line in 0..143) tick {
                /* oam */
                LY(line)
                STAT[1 downTo 0](2)

                val soam = process {
                    Register(10) {
                        class SOAM(val x: i8, val data: i8)
                        oam
                            .filter(it.y == LY)
                            .map {
                                SOAM(it.y)
                            }
                    }
                }
                for (dot in 0 .. 80) tick {}

                STAT[1 downTo 0](3)
                for (dot in 0 .. 160) tick {
                    val pixel: Int
                    process("pixel") {
                        pixel = 123
                    }

                    display(pixel)
                }

                /* hblank */
                STAT[1 downTo 0](0)
                for (dot in 260 .. 456) tick {}

            }

            /* vblank */
            STAT[1 downTo 0](1)
            for (line in 144..153) tick {
                LY(line)
                for (dot in 0..456) tick {}
            }
        }
    }
}

class ALU(
    op: Int,
    a: Int,
    b: Int
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

    init {
        val PC = 0.i16

        forever {
            val fetch = process {
                io.addr(PC)
                io.data
            }
            val exec = process {
                val decode = Decode(fetch())
                val writeBack = i8()
                if (decode.isALU) tick {
                    val alu = ALU(decode.op, decode.a, decode.b)

                    writeBack(alu.result)
                }
            }
            val write = process {
                io.data(exec())
                io.addr(PC)
            }
        }
    }
}

class CPU {
    val io: IO16x8

    init {
        val PC = 0.i16

        forever {
            val data = tick {
                io.addr(PC)
                PC(PC + 1)

                io.data
            }

            val decode = Decode(io.data)
            if (decode.isALU) tick {
                val alu = ALU(decode.op, decode.a, decode.b)

                io.addr(PC)
                io.data(exec)
            }
        }
    }
}

class GB {
    init {
        val cpu = CPU()
        val APU = PPU(apu.io)

        forever {}
    }
}