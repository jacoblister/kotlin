interface BitvecSignal : Bitvec, Signal {
    operator fun get(range: IntProgression): BitvecSlice = BitvecSlice(this, range.first, range.last)
    fun frombits(length: Int, bits: ULong): BitvecSignal
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
        (this.signal as Signal)(this.signal.frombits(this.signal.length, replacebits(this.signal.rawbits, from, to, next.rawbits)))
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

    operator fun invoke(next: intbv) = (this as Signal)(next)
    operator fun plus(other: intbv): intbv = intbv(this.length, this.value + other.value)

    override fun toString() = "$value:$length"
}

class i8(val value: Int) : BitvecSignal {
    override val length: Int get() = 8
    override val rawbits: ULong get() = value.toULong()
    override fun frombits(length: Int, bits: ULong): BitvecSignal = i8(bits.toInt())

    operator fun invoke(next: i8) = (this as Signal)(next)
    operator fun plus(other: i8): i8 = i8(this.value + other.value)

    override fun toString() = "$value"
}

val Int.i8 get() = i8(this)