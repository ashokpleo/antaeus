package io.pleo.antaeus.core.invoices.application.models

import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money

data class InvoiceResult(
    val id: Int,
    val customerId: Int,
    val amount: Money,
    val status: InvoiceStatus
)