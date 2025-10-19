class SPI() {
    var cs: bit
    var clk: bit
    var miso: bit
    var mosi: bit

    suspend fun sendRecieve(srcClk: Boolean, txData: Int, txLen: Int, rxData: Int, rxLen: Int) {
        while (srcClk() != 0) {}
        clk(0)
        cs(0)

        for (val i in 0..txLen) {
            while (srcClk() != 1) {}
            clk(1)

            mosi(txData[i])

            while (srcClk() != 0) {}
            clk(0)
        }

        for (val i in 0..txLen) {
            while (srcClk() != 1) {}
            clk(1)

            rxData[i] = miso()

            while (srcClk() != 0) {}
            clk(0)
        }

        cs(1)
    }
}