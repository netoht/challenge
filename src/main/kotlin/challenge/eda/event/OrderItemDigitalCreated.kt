package challenge.eda.event

import challenge.model.Invoice
import challenge.model.OrderItem

class OrderItemDigitalCreated(invoice: Invoice, item: OrderItem) : OrderItemCreated(invoice, item)