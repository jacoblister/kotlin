class SignalNone() : Signal
var pendingTarget: Signal = SignalNone()
var pendingNext: Signal = SignalNone()

interface Signal {
    operator fun invoke(next: Signal) {
        println("send")
        pendingTarget = this
        pendingNext = next
    }
//    override fun toString() = "<${signal!!::class.simpleName}>(signal=$this, next=$pending)"
}

fun updateSignal(signal: Signal, next: Signal) {
    for (field in signal.javaClass.declaredFields) {
        field.isAccessible = true
        val value = field.get(next)
        field.set(signal, value)

        println(field)
    }
}

fun signalNext() {
    updateSignal(pendingTarget, pendingNext)
}