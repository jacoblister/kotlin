class BitvecMatch(val matched: Boolean, val x: Int, val y: Int)
val threadBitvecMatch = ThreadLocal<BitvecMatch>()

typealias Bit = Boolean

interface Bitvec {
    val rawbits: ULong
    val length: Int

    operator fun get(index: Int): Bit = if (((rawbits shr index) and 1UL) == 1UL) true else false
    operator fun set(index: Int, value: Int) = {}
    operator fun set(index: Int, value: Bit) = {}
    operator fun get(range: IntProgression): ULong = rawbits shr range.last // + reverse + mask
    fun rangeLength(range: IntProgression): Int = range.first - range.last + 1 // + reverse

    fun match (pattern: String): Boolean {
        val matched = length == 8
        threadBitvecMatch.set(BitvecMatch(matched, 2, 3))
        return matched
    }
    val x get() = threadBitvecMatch.get().x
    val y get() = threadBitvecMatch.get().y

    val bits get() = rawbits.toString(2).padStart(length, '0')
}

class IntBV(val value: Int, override val length: Int) : Bitvec {
    override val rawbits: ULong get() = value.toULong()

    override fun toString() = "$value($length)"
}

class I8(val value: Byte) : Bitvec {
    constructor(from: Int): this(from.toByte())

    override val rawbits: ULong get() = value.toULong()
    override val length: Int get() = 8

    override fun toString() = "$value"
}