package challenge.eda.subscriber

import challenge.eda.Topics.EMAIL_NOTIFICATION_CREATED
import challenge.eda.Topics.ORDER_ITEM_DIGITAL_CREATED
import challenge.eda.event.EmailNotificationCreated
import challenge.eda.event.OrderItemDigitalCreated
import challenge.infra.eda.Message
import challenge.infra.eda.PubSub
import challenge.infra.eda.Subscriber
import challenge.infra.logger.LoggerFactory
import java.util.*

/**
 * Caso o pagamento seja de alguma mídia digital (música, vídeo), além de:
 * - enviar a descrição da compra por e-mail ao comprador,
 * - conceder um voucher de desconto de R$ 10 ao comprador associado ao pagamento.
 */
class OrderItemDigitalCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(ORDER_ITEM_DIGITAL_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val orderItem = message.body
        if (orderItem is OrderItemDigitalCreated) {

            notifyCustomerByEmail(orderItem, generatePromotionalCode(orderItem.invoice.order.customer.email))

        }
    }

    private fun generatePromotionalCode(email: String): String {
        val code = UUID.randomUUID().toString().substring(0, 5)
        logger.info("msg=generating promotional code associated with customer, email=$email, code=$code")
        return code
    }

    private fun notifyCustomerByEmail(
        orderItem: OrderItemDigitalCreated,
        promotionalCode: String
    ) {
        logger.info("msg=sending email, order=${orderItem.invoice.order.id}, customer=${orderItem.invoice.order.customer}, item=${orderItem.item.product.name}")
        PubSub.publish(
            EMAIL_NOTIFICATION_CREATED.name, Message(
                EmailNotificationCreated(
                    recipient = orderItem.invoice.order.customer.email,
                    subject = "Your order ${orderItem.invoice.order.id} has been paid successfully",
                    body = "Save with this promotional code ${promotionalCode} valid in 2019 on next purchase. Download now and enjoy your product: ${orderItem.item.product.name}. "
                )
            )
        )
    }
}
