package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.invoices.application.service.InvoiceServiceImpl
import io.pleo.antaeus.core.invoices.application.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.core.invoices.application.exceptions.UnableToCreateInvoiceException
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InvoiceServiceTest {
    private val genericInvoice1 = Invoice(
        id = 100,
        customerId = 1,
        amount = Money(BigDecimal(100), Currency.DKK),
        status = InvoiceStatus.PENDING,
        active = true
    )

    private val genericInvoice2 = Invoice(
        id = 101,
        customerId = 1,
        amount = Money(BigDecimal(100), Currency.DKK),
        status = InvoiceStatus.PAID,
        active = true
    )

    private val invoiceRepo = mockk<InvoiceRepo> {
        every { fetchInvoice(404) } returns null
        every { createInvoice(
            amount = (Money(BigDecimal(100), Currency.DKK)),
            customer = Customer(id = 404, currency = Currency.DKK)
        )} returns null
        every { fetchInvoice(100) } returns genericInvoice1
        every { fetchInvoices() } returns listOf(genericInvoice1, genericInvoice2)
    }

    private val invoiceService = InvoiceServiceImpl(invoiceRepo = invoiceRepo)

    @Test
    fun `will throw if invoice is not found`() {
        assertThrows<InvoiceNotFoundException> {
            invoiceService.fetch(404)
        }
    }

    @Test
    fun `will throw if invoice is not not created`() {
        assertThrows<UnableToCreateInvoiceException> {
            invoiceService.create(404, Currency.DKK, Money(BigDecimal(100), Currency.DKK))
        }
    }

    @Test
    fun `should be able to fetch single a invoice`() {
        // When
        val invoice = invoiceService.fetch(100)

        // Then
        assertInvoice(invoice, genericInvoice1)
    }

    @Test
    fun `should be able to fetch all invoices`() {
        // When
        val invoices = invoiceService.fetchAll()

        // Then
        assertInvoice(invoices[0], genericInvoice1)
        assertInvoice(invoices[1], genericInvoice2)
    }

    private fun assertInvoice(invoices: InvoiceResult, expectedInvoice: Invoice) {
        assert(invoices.id == expectedInvoice.id)
        assert(invoices.amount == expectedInvoice.amount)
        assert(invoices.status == expectedInvoice.status)
        assert(invoices.customerId == expectedInvoice.customerId)
    }
}
