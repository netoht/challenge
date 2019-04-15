package challenge.eda.event

import challenge.eda.Topics
import challenge.model.Invoice
import challenge.model.OrderItem
import challenge.model.ProductType.*

abstract class OrderItemCreated(val invoice: Invoice, val item: OrderItem) {

    companion object {
        fun create(invoice: Invoice, item: OrderItem) = when (item.product.type) {
            PHYSICAL -> Pair(Topics.ORDER_ITEM_PHYSICAL_CREATED, OrderItemPhysicalCreated(invoice, item))
            BOOK -> Pair(Topics.ORDER_ITEM_BOOK_CREATED, OrderItemBookCreated(invoice, item))
            DIGITAL -> Pair(Topics.ORDER_ITEM_DIGITAL_CREATED, OrderItemDigitalCreated(invoice, item))
            MEMBERSHIP -> Pair(Topics.ORDER_ITEM_MEMBERSHIP_CREATED, OrderItemMembershipCreated(invoice, item))
        }
    }
}