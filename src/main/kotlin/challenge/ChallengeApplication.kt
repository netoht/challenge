package challenge

import challenge.eda.Topics
import challenge.eda.event.OrderCreated
import challenge.eda.subscriber.*
import challenge.infra.eda.Message
import challenge.infra.eda.PubSubService
import challenge.model.*
import java.util.concurrent.TimeUnit

private fun bootstrap(): Order {
    val shirt = Product("Flowered t-shirt", ProductType.PHYSICAL, 35.00)
    val netflix = Product("Familiar plan", ProductType.MEMBERSHIP, 29.90)
    val book = Product("The Hitchhiker's Guide to the Galaxy", ProductType.BOOK, 120.00)
    val music = Product("Stairway to Heaven", ProductType.DIGITAL, 5.00)

    val order = Order(Customer("Bob Alice", "bob@alice.com"), Address())

    order.addProduct(shirt, 2)
    order.addProduct(netflix, 1)
    order.addProduct(book, 1)
    order.addProduct(music, 1)

    order.pay(CreditCard("43567890-987654367"))

    return order
}

private fun configurePubSub() {
    PubSubService.init(
        EmailNotificationCreatedSubscriber(),
        OrderCreatedSubscriber(),
        OrderItemBookCreatedSubscriber(),
        OrderItemDigitalCreatedSubscriber(),
        OrderItemMembershipCreatedSubscriber(),
        OrderItemPhysicalCreatedSubscriber()
    )
}

private fun exit() {
    TimeUnit.SECONDS.sleep(2)
    System.exit(0)
}

fun main() {
    configurePubSub()
    bootstrap().process { invoice, items ->
        PubSubService.publish(Topics.ORDER_CREATED.name, Message(OrderCreated(invoice, items)))
    }
    exit()
}