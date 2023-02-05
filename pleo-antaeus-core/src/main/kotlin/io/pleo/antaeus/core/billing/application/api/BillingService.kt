package io.pleo.antaeus.core.billing.application.api

import io.pleo.antaeus.core.billing.application.models.BillResult

interface BillingService {
    fun chargeInvoice(invoiceId: Int): BillResult
}
