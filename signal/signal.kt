class Signal<T>(var value: T) {
    var next: T

	override fun toString(): String {
	    return "Wire(signal=$signal)"
	}
    fun send(message: T) {

    }
    operator fun invoke(): T {
        return value
    }

    operator fun invoke(message: Wire<T>) {
    }
    fun recv(): T {
        return value
    }
}

typealias f32 = Signal<Float>
fun f32(): f32 { return f32(0.0f) }
operator fun f32.plus(other: f32): f32 { return f32(signal + other.signal) }
operator fun f32.plus(other: Float): f32 { return f32(signal + other) }
operator fun Float.plus(other: f32): f32 { return f32(this + other.signal) }
operator fun f32.compareTo(other: Float): Int { return 0 }
operator fun f32.compareTo(other: f32): Int { return 0 }
operator fun f32.minus(other: Float): f32 { return f32(signal + other) }
operator fun f32.times(other: f32): f32 { return f32(signal * other.signal) }

// typealias f32 = Channel<Float>
// fun f32(): f32 { return Channel<Float>(capacity = 1) }
// suspend fun f32(signal: Float): f32 { val c = Channel<Float>(capacity = 1); c.send(signal); return c }
// suspend operator fun f32.invoke(message: Float) { this.send(message)}
// suspend operator fun f32.plus(other: f32): Float { return other.receive() }

fun latch(initial: Float, action: () -> f32): Float {
      return initial
}

fun parameter(level: Float, min: Double = 0.0, max: Double = 1.0): f32 {
    return f32(level)
}

fun process(action: () -> Unit) {

}

class Envelope(
    val trigger: float,
    val attack: float = parameter(1.0, min = 0.1, max = 1000.0),
    val decay: float = parameter(10.0, min = 0.1, max = 1000.0),
    val sustain: float = parameter(0.7, min = 0.0, max = 1.0),
    val release: float = parameter(1000.0, min = 0.1, max = 10000.0)
) {
    val output: float = float()

    init {
        forever {
            while (trigger < 1) {
                output(0)
            }
            for (level in 0..1 step { attack * 44100 / 1000 }) {
                output(level)
            }
            for (level in 1..sustain step { decay * 44100 / 1000 }) {
                output(level)
            }
            while(trigger > 0) {
                output(sustain)
            }
            for (level in sustain..0 step { release * 44100 / 1000 }) {
                output(level)
            }
        }
    }

    fun render() {
        Panel {
            Knob(src = attack, label="Attack")
            Knob(src = decay, label="Decay")
            Knob(src = sustain, label="Sustain")
            Knob(src = release, label="Release")
        }
    }
}

class Oscillator(
    val freq: float = parameter(440, min = 20, max = 200000)
) {
    val output = float()
    init {
        val phase: float = 0.f

        forever {
            phase += process { freq() / 1000 } 
            output(phase)
        }
    }
}

class Gain(
    val left: float,
    val right: float
) {
    val output = float()
    init {
        output = left + right
    }
}

class Synth() {
	val output = float()

    val oscillator = Oscillator()
    val envelope = Envelope(attack = 20.f, release = midicc(chan = 1, cc = 7, min = 0.0, max = 1000.0))
    val gain = Gain(left = oscillator.output, right = envelope.output)

    operator fun invoke() {
        output(gain.output)
    }
}

class Delay {
    var sample = float.Array(44100)
}

fun main() {
    val envelope = Envelope(attack = 2.f)
    println(envelope.output)

    println(f32(1.1f) + f32(2.2f))
    println(f32(1.1f) + 2.2f)
    println(1.1f + f32(2.2f))

    println(1.f + 2.f)
    println(1.1.f + 2.2.f)

}