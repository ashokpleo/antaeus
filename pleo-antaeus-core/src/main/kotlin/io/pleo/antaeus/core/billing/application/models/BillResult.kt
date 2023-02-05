package io.pleo.antaeus.core.billing.application.models

import io.pleo.antaeus.core.customers.application.models.CustomerResult
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult

data class BillResult (val invoice: InvoiceResult, val customer: CustomerResult, val chargeSucceeded: Boolean? = null)
