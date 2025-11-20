class BitvecMatch(val matched: Boolean, val x: Int, val y: Int)
val threadBitvecMatch = ThreadLocal<BitvecMatch>()

typealias Bit = Boolean

interface Bitvec {
    val rawbits: ULong
    val length: Int

    operator fun get(index: Int): Bit = if (((rawbits shr index) and 1UL) == 1UL) true else false
    operator fun set(index: Int, value: Int) = {}
    fun set(index: Int, value: Bit) = {}
    fun get(range: IntProgression): ULong = rawbits shr range.last // + reverse + mask
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