package challenge.infra.eda

interface PubSub {
    fun init(vararg subscribers: Subscriber)
    fun publish(topic: String, message: Message)
}