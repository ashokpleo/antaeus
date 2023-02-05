package io.pleo.antaeus.core.invoices.application.service

import com.google.inject.Inject
import io.pleo.antaeus.core.invoices.application.api.InvoiceService
import io.pleo.antaeus.core.invoices.application.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.core.invoices.application.exceptions.UnableToCreateInvoiceException
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.core.invoices.application.models.toResult
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.models.*

class InvoiceServiceImpl @Inject constructor(private val invoiceRepo: InvoiceRepo): InvoiceService {
    override fun fetchAll(): List<InvoiceResult> {
        return invoiceRepo.fetchInvoices().map { it.toResult() }
    }

    override fun fetchAllActive(): List<InvoiceResult> {
        return invoiceRepo.fetchActiveInvoices().map { it.toResult() }
    }

    override fun fetch(id: Int): InvoiceResult {
        return invoiceRepo.fetchInvoice(id)?.toResult() ?: throw InvoiceNotFoundException(id)
    }

    override fun create(customerId: Int, currency: Currency, amount: Money, status: InvoiceStatus): InvoiceResult {
        return invoiceRepo.createInvoice(amount, Customer(customerId, currency), status)?.toResult() ?: throw UnableToCreateInvoiceException(customerId, amount.value)
    }

    override fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): InvoiceResult {
        invoiceRepo.updateStatus(invoiceId, invoiceStatus)
        return fetch(invoiceId)
    }
}