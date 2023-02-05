package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.billing.application.integration.models.ChargeResponse
import io.pleo.antaeus.core.billing.application.models.Bill
import io.pleo.antaeus.core.billing.application.service.BillingServiceImpl
import io.pleo.antaeus.core.billing.application.spi.PaymentProvider
import io.pleo.antaeus.core.customers.application.api.CustomerService
import io.pleo.antaeus.core.customers.application.models.CustomerResult
import io.pleo.antaeus.core.invoices.application.api.InvoiceService
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BillingServiceTest {
    private val invoice100 = InvoiceResult(
        id = 100,
        customerId = 1,
        amount = Money(BigDecimal(100), Currency.DKK),
        status = InvoiceStatus.PENDING
    )

    private val invoice101 = InvoiceResult(
        id = 101,
        customerId = 1,
        amount = Money(BigDecimal(100), Currency.DKK),
        status = InvoiceStatus.PENDING
    )
    private val customer1 = CustomerResult(id = 1, currency = Currency.DKK)

    private val invoiceService = mockk<InvoiceService> {
        every { fetch(100) } returns invoice100
        every { updateStatus(100, InvoiceStatus.PAID)} returns invoice100.copy(status = InvoiceStatus.PAID)

        every { fetch(101) } returns invoice101
        every { updateStatus(101, InvoiceStatus.PENDING)} returns invoice101
    }
    private val customerService = mockk<CustomerService> {
        every { fetch(1) } returns customer1
    }

    private val payments = mockk<PaymentProvider> {
        every { charge(Bill(invoice100, customer1))} returns ChargeResponse(true)
        every { charge(Bill(invoice101, customer1))} returns ChargeResponse(false)
    }


    private val billingService = BillingServiceImpl(payments, customerService, invoiceService)

    @Test
    fun `be able to charge invoice`() {
        // when
        val bill = billingService.chargeInvoice(100)

        // then
        assert(bill.chargeSucceeded == true)
        assert(bill.invoice.status == InvoiceStatus.PAID)
    }

    @Test
    fun `have invoice status of Pending if payment failed`() {
        // when
        val bill = billingService.chargeInvoice(101)

        // then
        assert(bill.chargeSucceeded == false)
        assert(bill.invoice.status == InvoiceStatus.PENDING)
    }
}
