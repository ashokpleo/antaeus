/*
    Implements endpoints related to invoices.
 */

package io.pleo.antaeus.core.services

import com.google.inject.Inject
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice


interface InvoiceService {
    fun fetchAll(): List<Invoice>

    fun fetch(id: Int): Invoice
}


class InvoiceServiceImpl @Inject constructor(private val dal: AntaeusDal): InvoiceService {
    override fun fetchAll(): List<Invoice> {

        return dal.fetchInvoices()
    }

    override fun fetch(id: Int): Invoice {
        return dal.fetchInvoice(id) ?: throw InvoiceNotFoundException(id)
    }
}
