class BitvecMatch(val matched: Boolean, val x: Int, val y: Int)
val threadBitvecMatch = ThreadLocal<BitvecMatch>()

typealias Bit = Boolean

interface Bitvec {
    val length: Int
    val rawbits: ULong

    operator fun get(index: Int): Bit = if (((rawbits shr index) and 1UL) == 1UL) true else false

    fun match (pattern: String): Boolean {
        val matched = length == 8
        threadBitvecMatch.set(BitvecMatch(matched, 2, 3))
        return matched
    }
    val x get() = threadBitvecMatch.get().x
    val y get() = threadBitvecMatch.get().y

    val bits get() = rawbits.toString(2).padStart(length, '0')
}