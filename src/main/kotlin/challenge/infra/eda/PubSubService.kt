package challenge.infra.eda

import challenge.infra.logger.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

object PubSubService : PubSub, Runnable {

    private data class PublishRequest(val topic: String, val message: Message)

    private val logger = LoggerFactory.getLogger()
    private val executor = Executors.newFixedThreadPool(4)
    private val queue = LinkedBlockingQueue<PublishRequest>()
    private val subscribers = ConcurrentHashMap<String, Set<Subscriber>>()

    init {
        executor.execute(this)
    }

    override fun init(vararg subscribers: Subscriber) {
        subscribers.forEach {
            it.subscribe().forEach { topic ->
                addSubscriber(topic, it)
            }
        }
    }

    private fun addSubscriber(topic: String, subscriber: Subscriber) {
        subscribers.merge(topic, CopyOnWriteArraySet<Subscriber>().apply { add(subscriber) }) { acc, new ->
            acc.toMutableList().addAll(new)
            acc
        }
    }

    override fun publish(topic: String, message: Message) {
        subscribers.getOrElse(topic) {
            throw IllegalArgumentException("doesn't exists subscribers for the topic, topic=$topic")
        }
        queue.add(PublishRequest(topic, message))
    }

    override fun run() {
        while (true) {
            var publishRequest: PublishRequest
            try {
                publishRequest = queue.take()
            } catch (e: InterruptedException) {
                continue
            }

            subscribers[publishRequest.topic]!!.parallelStream().forEach {
                executor.execute {
                    try {
                        it.receiveMessage(publishRequest.topic, publishRequest.message)
                    } catch (e: Exception) {
                        logger.severe("msg=error processing message, topic=${publishRequest.topic}, message=${publishRequest.message}, exception=$e")
                    }
                }
            }
        }
    }
}
