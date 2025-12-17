class SPI(
    @Out val cs: bit,
    @Out val mosi: bit,
    @In  val miso: bit
) {
    @Out val txData: bit = bit()
    @In  val txData: bit = bit()

    init {
        forever({txData}) {
            for (val data in txData) tick {
                mosi(data)
            }

            for (val data in rxData) tick {
                data(mosi)
            }
        }
    }
}

fun main() {
    val spi = SPI(cs = Pin[10], mosi = Pin[11], miso = Pin[12])

    val txMsg = 0x1A.i8
    val rxMsg = i8()

    spi.cs(0)
    spi.txData(txMsg.bits)
    spi.rxData(rxMsg.bits)
    spi.cs(1)

    spi.txData(i8.array(0x2A, 0x00, 0x01))
}