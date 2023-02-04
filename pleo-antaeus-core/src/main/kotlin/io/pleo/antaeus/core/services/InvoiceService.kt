/*
    Implements endpoints related to invoices.
 */

package io.pleo.antaeus.core.services

import com.google.inject.Inject
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.models.*


interface InvoiceService {
    fun fetchAll(): List<Invoice>

    fun fetchAllUnpaid(): List<Invoice>

    fun fetch(id: Int): Invoice

    fun create(customerId: Int, currency: Currency, amount: Money, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice?

    fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): Invoice
}


class InvoiceServiceImpl @Inject constructor(private val invoiceRepo: InvoiceRepo): InvoiceService {
    override fun fetchAll(): List<Invoice> {
        return invoiceRepo.fetchInvoices()
    }

    override fun fetchAllUnpaid(): List<Invoice> {
        return invoiceRepo.fetchUnpaidInvoices()
    }

    override fun fetch(id: Int): Invoice {
        return invoiceRepo.fetchInvoice(id) ?: throw InvoiceNotFoundException(id)
    }

    override fun create(customerId: Int, currency: Currency, amount: Money, status: InvoiceStatus): Invoice? {
        return invoiceRepo.createInvoice(amount, Customer(customerId, currency), status)
    }

    override fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): Invoice {
        updateStatus(invoiceId, invoiceStatus)
        return fetch(invoiceId)
    }
}
