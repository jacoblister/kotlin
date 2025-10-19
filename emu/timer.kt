class Timer(
    tick: boolean
) {
    val interrupt: int

    fun process() {
        val ticks: i16

        val DIV: i8 = 0
        val TIMA: i8 = 0
        val TMA: i8 = 0
        val TAC: i8 = 0

        forever("timer") {
            tick()

            if (ticks[7 downTo 0] == 0) {
                DIV(DIV + 1)
            }

            val clockMask = TAC[1 downTo 0] + 3
            if (TAC[1] && ticks[clockMask]) {
                if (TIMA == 0) {
                    TIMA = TMA
                    interrupt(interrupt or 4)
                }
                TIMA(TIMA + 1)
            }

            ticks(ticks + 1)
        }
    }
}