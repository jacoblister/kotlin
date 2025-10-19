abstract class Signal<T> (
    var signal: T,
    var next: T
) {
    fun send(msg: T) {
        next = msg
        println("Send $msg")
    }

    override fun toString() = "<${signal!!::class.simpleName}>(signal=$signal, next=$next)"
//    override fun toString() = "<>(signal=$signal, next=$next)"
}

fun IntBV_from_array_intbv(bitvecs: Array<out intbv>): IntBV {
    var value : Int = 0
    var length: Int = 0
    for (bitvec in bitvecs) {
        value = (value shl length) or bitvec.signal.value
        length += bitvec.signal.length
    }

    return IntBV(value, length)
}
class intbv(value: IntBV): Signal<IntBV>(value, value) {
    constructor(value: Int, length: Int): this(IntBV(value, length))
    constructor(vararg bitvecs: intbv): this(IntBV_from_array_intbv(bitvecs))

    operator fun get(range: IntProgression): intbv = intbv(this.signal.get(range).toInt(), this.signal.rangeLength(range))
    operator fun plus(other: intbv): intbv = intbv(this.signal.value + other.signal.value, this.signal.length)
}

class i8(value: I8): Signal<I8>(value, value) {
    constructor(value: Int): this(I8(value))

    operator fun get(index: Int): Bit = signal.get(index)
    operator fun set(index: Int, value: Int) = signal.set(index, value)
    operator fun set(index: Int, value: Bit) = signal.set(index, value)

    operator fun invoke(msg: Int) = this.send(I8(msg))
    operator fun invoke(msg: i8) = this.send(msg.signal)
    operator fun plus(other: i8): i8 = i8(this.signal.value + other.signal.value)
}
val Int.i8 get() = i8(this)