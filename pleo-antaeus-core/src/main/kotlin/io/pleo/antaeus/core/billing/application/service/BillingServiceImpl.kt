package io.pleo.antaeus.core.billing.application.service

import com.google.inject.Inject
import io.pleo.antaeus.core.billing.application.api.BillingService
import io.pleo.antaeus.core.billing.application.integration.models.ChargeResponse
import io.pleo.antaeus.core.customers.application.api.CustomerService
import io.pleo.antaeus.core.invoices.application.api.InvoiceService
import io.pleo.antaeus.core.billing.application.spi.PaymentProvider
import io.pleo.antaeus.core.billing.application.models.Bill
import io.pleo.antaeus.core.billing.application.models.BillResult
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class BillingServiceImpl @Inject constructor(
    private val payments: PaymentProvider,
    private val customerService: CustomerService,
    private val invoiceService: InvoiceService
) : BillingService {
    private val logger = KotlinLogging.logger {}

    override fun chargeInvoice(invoiceId: Int): BillResult {
        logger.info("Charge request for Invoice $invoiceId")

        val invoice = invoiceService.fetch(invoiceId)
        val customer = customerService.fetch(invoice.customerId)
        val bill = Bill(invoice = invoice, customer = customer)

        logger.info("Attempting to charge customer ${customer.id} for ${invoice.amount.value}${invoice.amount.currency}")
        val payment = payments.charge(bill)

        val updatedInvoice = updateInvoiceStatus(payment, invoiceId)

        logger.info("Payment completed for customer:invoice ${customer.id}:${invoiceId} payment succeeded:${payment.succeeded}")
        return BillResult(updatedInvoice, customer, payment.succeeded)
    }

    private fun updateInvoiceStatus(
        payment: ChargeResponse,
        invoiceId: Int
    ): InvoiceResult {
        return if (payment.succeeded)
            invoiceService.updateStatus(invoiceId, InvoiceStatus.PAID)
        else invoiceService.updateStatus(invoiceId, InvoiceStatus.PENDING)
    }
}