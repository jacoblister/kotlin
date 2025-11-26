interface Bitvec {
    val length: Int
    companion object match {
        var matched = true
        var matchedX = Bitvec(this.length, 0)
        var matchedY = bitvec(this.length, 0)

        fun match(pattern: String): bitvec {
            matchedX = bitvec(this.length, 1)
            matchedY = bitvec(this.length, 2)
            return bitvec(this.length, 0)
        }
    }
}

class I8(var value: Int): Bitvec {
    override val length = 8
}

fun main() {
    val byte = "10000001".Bitvec

    val nibble = byte[3 downTo 0]

    val inst = when(byte) {
        in "00xxyy.1" -> "ADD $byte.x $byte.y"
    }
}