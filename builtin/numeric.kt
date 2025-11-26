class i8(val value: Int) : BitvecSignal {
    override val length: Int get() = 8
    override val rawbits: ULong get() = value.toULong()
    override fun frombits(length: Int, bits: ULong): BitvecSignal = i8(bits.toInt())

    operator fun invoke(next: i8) = this.next(next)
    operator fun plus(other: i8): i8 = i8(this.value + other.value)

    override fun toString() = "$value"
}

val Int.i8 get() = i8(this)