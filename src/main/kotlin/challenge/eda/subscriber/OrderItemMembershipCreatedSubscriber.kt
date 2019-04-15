package challenge.eda.subscriber

import challenge.eda.Topics.EMAIL_NOTIFICATION_CREATED
import challenge.eda.Topics.ORDER_ITEM_PHYSICAL_CREATED
import challenge.eda.event.EmailNotificationCreated
import challenge.eda.event.OrderItemMembershipCreated
import challenge.infra.eda.Message
import challenge.infra.eda.PubSub
import challenge.infra.eda.Subscriber
import challenge.infra.logger.LoggerFactory

/**
 * Caso o pagamento seja uma assinatura de serviço, você precisa:
 * - ativar a assinatura;
 * - notificar o usuário através de e-mail sobre isto;
 */
class OrderItemMembershipCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(ORDER_ITEM_PHYSICAL_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val orderItem = message.body
        if (orderItem is OrderItemMembershipCreated) {
            logger.info("msg=activating subscription, order=${orderItem.invoice.order.id}, customer=${orderItem.invoice.order.customer}, item=${orderItem.item.product.name}")

            notifyCustomerByEmail(orderItem)
        }
    }

    private fun notifyCustomerByEmail(orderItem: OrderItemMembershipCreated) {
        logger.info("msg=sending email, order=${orderItem.invoice.order.id}, customer=${orderItem.invoice.order.customer}, item=${orderItem.item.product.name}")
        PubSub.publish(
            EMAIL_NOTIFICATION_CREATED.name, Message(
                EmailNotificationCreated(
                    recipient = orderItem.invoice.order.customer.email,
                    subject = "Your account has been activated",
                    body = "Congratulations ${orderItem.invoice.order.customer.name}, your account has been successfully activated!"
                )
            )
        )
    }
}
