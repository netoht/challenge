package challenge.eda.subscriber

import challenge.eda.Topics
import challenge.eda.event.OrderItemBookCreated
import challenge.infra.eda.Message
import challenge.infra.eda.Subscriber
import challenge.infra.logger.LoggerFactory

/**
 * Caso o pagamento seja um livro comum, ? preciso gerar o 'shipping label' com uma notifica??o de
 * que trata-se de um item isento de impostos conforme disposto na Constitui??o Art. 150, VI, d.
 */
class OrderItemBookCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(Topics.ORDER_ITEM_BOOK_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val orderItem = message.body
        if (orderItem is OrderItemBookCreated) {
            logger.info("msg=generating a shipping label with tax free, order=${orderItem.invoice.order.id}, item=${orderItem.item.product.name}")
        }
    }
}
