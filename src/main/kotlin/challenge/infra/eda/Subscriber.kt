package challenge.infra.eda

interface Subscriber {
    fun subscribe(): List<String>
    fun receiveMessage(topic: String, message: Message)
}
