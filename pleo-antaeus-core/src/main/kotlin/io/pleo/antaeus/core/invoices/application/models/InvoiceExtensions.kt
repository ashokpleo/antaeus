package io.pleo.antaeus.core.invoices.application.models

import io.pleo.antaeus.models.Invoice

fun Invoice.toResult(): InvoiceResult = InvoiceResult(this.id, this.customerId, this.amount, this.status)