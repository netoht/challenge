package challenge.eda.subscriber

import challenge.eda.Topics.ORDER_CREATED
import challenge.eda.event.OrderCreated
import challenge.eda.event.OrderItemCreated
import challenge.infra.eda.Message
import challenge.infra.eda.PubSub
import challenge.infra.eda.Subscriber
import challenge.infra.logger.LoggerFactory
import challenge.model.OrderItem

class OrderCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(ORDER_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val orderCreated = message.body
        if (orderCreated is OrderCreated) {
            logger.info("msg=order received, order=${orderCreated.invoice.order}")
            orderCreated.items.parallelStream().forEach {
                publishItems(orderCreated, it)
            }
        }
    }

    private fun publishItems(orderCreated: OrderCreated, it: OrderItem) {
        val (topic, event) = OrderItemCreated.create(orderCreated.invoice, it)
        logger.info("msg=sending order item, order=${orderCreated.invoice.order.id}, item=${it.product.name}")
        PubSub.publish(topic.name, Message(event))
    }
}
