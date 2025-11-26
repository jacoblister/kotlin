class BitvecMatch(val matched: Boolean, val x: Int, val y: Int)
val threadBitvecMatch = ThreadLocal<BitvecMatch>()

interface BitvecSignal : Signal {
    val length: Int
    val rawbits: ULong
    fun frombits(length: Int, bits: ULong): BitvecSignal

    operator fun get(index: Int): BitvecSlice = BitvecSlice(this, index, index)
    operator fun get(range: IntProgression): BitvecSlice = BitvecSlice(this, range.first, range.last)

    fun match (pattern: String): Boolean {
        val matched = length == 8
        threadBitvecMatch.set(BitvecMatch(matched, 2, 3))
        return matched
    }
    val x get() = threadBitvecMatch.get().x
    val y get() = threadBitvecMatch.get().y

    val bits get() = rawbits.toString(2).padStart(length, '0')
}

class BitvecSlice(val signal: BitvecSignal, val from: Int, val to: Int) : BitvecSignal {
    override val length: Int get() = from - to + 1
    override val rawbits: ULong get() = (signal.rawbits shr to) and ((1UL shl (from - to + 1)) - 1UL)
    override fun frombits(length: Int, bits: ULong): BitvecSignal = this

    fun replacebits(bits: ULong, from: Int, to: Int, nextbits: ULong): ULong {
        val mask = ((1UL shl (from - to + 1)) - 1UL)
        return (bits and mask) or (nextbits shl to)
    }

    operator fun invoke(next: BitvecSignal) {
        this.signal.next(this.signal.frombits(this.signal.length, replacebits(this.signal.rawbits, from, to, next.rawbits)))
//        this.next(next)
    }
}

class intbv : BitvecSignal {
    override val length: Int
    val value: Int
    override val rawbits: ULong get() = value.toULong()
    override fun frombits(length: Int, bits: ULong): BitvecSignal = intbv(length, bits.toInt())

    constructor(length: Int, value: Int) {
        this.length = length
        this.value = value
    }

    constructor(vararg bitvecs: intbv) {
        var initLength: Int = 0
        var initValue: Int = 0
        for (bitvec in bitvecs) {
            initLength += bitvec.length
            initValue = (initValue shl initLength) or bitvec.value
        }
        length = initLength
        value = initValue
    }

    operator fun invoke(next: intbv) = this.next(next)
    operator fun plus(other: intbv): intbv = intbv(this.length, this.value + other.value)

    override fun toString() = "$value:$length"
}