package io.pleo.antaeus.core.services

import com.google.inject.Inject
import io.pleo.antaeus.core.spi.Payments
import io.pleo.antaeus.models.CustomerStatus
import io.pleo.antaeus.models.InvoiceStatus

interface BillingService {
    fun chargeInvoice(invoiceId: Int): Boolean

}

class BillingServiceImpl @Inject constructor(
    private val payments: Payments,
    private val customerService: CustomerService,
    private val invoiceService: InvoiceService
) : BillingService{

    override fun chargeInvoice(invoiceId: Int): Boolean {
        val invoice = invoiceService.fetch(invoiceId)
        val customer = customerService.fetch(invoiceId)

        if (customer.status == CustomerStatus.CANCELLED)
            return false

        val payment = payments.charge(invoice, customer)

        if (payment.succeeded)
                invoiceService.updateStatus(invoiceId, InvoiceStatus.PAID)
        else invoiceService.updateStatus(invoiceId, InvoiceStatus.PENDING)

        return payment.succeeded
    }

}
