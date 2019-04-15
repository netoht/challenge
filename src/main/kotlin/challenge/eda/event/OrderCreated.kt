package challenge.eda.event

import challenge.model.Invoice
import challenge.model.OrderItem

data class OrderCreated(val invoice: Invoice, val items: List<OrderItem>)