fun logBitvec(bitvec: Bitvec) {
    println(bitvec)
}

fun testBitVecSignal() {
    val test_a = intbv(128, 8)
    val test_b = intbv(3, 8)

    println("$test_a + $test_b = ${test_a + test_b}")

    val nibble = test_a[7 downTo 4]
    println(nibble)

    val byte = intbv(nibble, nibble)
    println(byte)
    println(byte.signal.bits)
}

fun testValue() {
    val test_a = I8(2)
    val test_b = I8(3)

    println("$test_a + $test_b")
}

fun testSignal() {
    val test_a = i8(2)
    val test_b = 3.i8

    test_b(123)

    println("$test_a + $test_b = ${test_a + test_b}")
}

fun main() {
    testBitVecSignal()
//    testValue()
//    testSignal()

//    val bitvecInt = Bitvec_Int(0xFFF, 13)
//    val bitvecI8 = I8(0xFF)
//
//    println(bitvecI8.match("11111111"))
//    println(bitvecI8.x)
//
//    logBitvec(bitvecInt)
//    logBitvec(bitvecI8)
}