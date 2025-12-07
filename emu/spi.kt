class SPI() {
    var cs: bit
    var clk: bit = Clock
    var miso: bit
    var mosi: bit

    fun sendRecieve(txData: Int, txLen: Int, rxData: Int, rxLen: Int) {
        tick {
            cs(0)
        }

        for (val i in 0..txLen) tick {
            mosi(txData[i])
        }

        for (val i in 0..txLen) tick {
            rxData[i] = miso()
        }

        tick {
            cs(1)
        }
    }
}