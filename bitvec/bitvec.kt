class bitvec {
    companion object match {
        var matched = true
        var matchedX = bitvec(0)
        var matchedY = bitvec(0)

        fun match(pattern: String): bitvec {
            matchedX = bitvec(1)
            matchedY = bitvec(2)
            return bitvec(0)
        }
    }
    val bit: Int
    constructor (inital: String) {
        bit = 123
    }
    constructor (initial: Int) {
        bit = initial
    }
    override fun toString(): String = "bitvec = ${Integer.toBinaryString(bit).padStart(8, '0')}"
    operator fun plus(rhs: bitvec) = bitvec(bit + rhs.bit)
    operator fun rangeTo(rhs: bitvec) = bitvec("1111")
    operator fun get(index: Int) = (bit shr index) and 1
    operator fun get(start: Int, end: Int) = bit

    fun match(pattern: String): bitvec {
        return match.match(pattern)
    }
    operator fun contains(pattern: bitvec) = match.matched
    val x get() = match.matchedX
    val y get() = match.matchedY
}