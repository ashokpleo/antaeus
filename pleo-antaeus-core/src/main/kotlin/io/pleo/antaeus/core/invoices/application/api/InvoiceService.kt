package io.pleo.antaeus.core.invoices.application.api
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.models.*


interface InvoiceService {
    fun fetchAll(): List<InvoiceResult>

    fun fetchAllUnpaid(): List<InvoiceResult>

    fun fetch(id: Int): InvoiceResult

    fun create(customerId: Int, currency: Currency, amount: Money, status: InvoiceStatus = InvoiceStatus.PENDING): InvoiceResult

    fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): InvoiceResult
}

