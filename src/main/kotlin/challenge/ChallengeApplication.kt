package challenge

import challenge.eda.Topics
import challenge.eda.event.OrderCreated
import challenge.eda.subscriber.*
import challenge.infra.eda.Message
import challenge.infra.eda.PubSub
import challenge.model.*

fun main() {
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
    // now, how to deal with shipping rules then?

    PubSub.init(
        EmailNotificationCreatedSubscriber(),
        OrderCreatedSubscriber(),
        OrderItemBookCreatedSubscriber(),
        OrderItemDigitalCreatedSubscriber(),
        OrderItemMembershipCreatedSubscriber(),
        OrderItemPhysicalCreatedSubscriber()
    )

    order.process { invoice, items ->
        PubSub.publish(Topics.ORDER_CREATED.name, Message(OrderCreated(invoice, items)))
    }
}