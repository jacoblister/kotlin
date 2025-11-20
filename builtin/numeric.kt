class intbv : Bitvec, Signal {
    val value: Int
    override val length: Int

    constructor(value: Int, length: Int) {
        this.value = value
        this.length = length
    }

    constructor(vararg bitvecs: intbv) {
        var initValue: Int = 0
        var initLength: Int = 0
        for (bitvec in bitvecs) {
            initValue = (initValue shl initLength) or bitvec.value
            initLength += bitvec.length
        }
        value = initValue
        length = initLength
    }

    override val rawbits: ULong get() = value.toULong()

    //    operator fun get(range: IntProgression): intbv(this.get(range).toInt(), this.rangeLength(range))
    operator fun plus(other: intbv): intbv = intbv(this.value + other.value, this.length)

    override fun toString() = "$value:$length"
}

class i8(val value: Int) : Bitvec, Signal {
    override val rawbits: ULong get() = value.toULong()
    override val length: Int get() = 8

//    operator fun get(index: Int): Bit = value.get(index)

    operator fun plus(other: i8): i8 = i8(this.value + other.value)

    override fun toString() = "$value"
}

val Int.i8 get() = i8(this)