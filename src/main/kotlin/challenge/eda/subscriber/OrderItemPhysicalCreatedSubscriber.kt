package challenge.eda.subscriber

import challenge.infra.eda.Message
import challenge.infra.eda.Subscriber
import challenge.eda.Topics.ORDER_ITEM_PHYSICAL_CREATED
import challenge.eda.event.OrderItemPhysicalCreated
import challenge.infra.logger.LoggerFactory

/**
 * Se o pagamento for para um item físico, deverá gerar
 * um 'shipping label' para o mesmo ser colocado na caixa do envio
 */
class OrderItemPhysicalCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(ORDER_ITEM_PHYSICAL_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val orderItem = message.body
        if (orderItem is OrderItemPhysicalCreated) {
            logger.info("msg=generating a shipping label, order=${orderItem.invoice.order.id}, item=${orderItem.item.product.name}")
        }
    }
}
