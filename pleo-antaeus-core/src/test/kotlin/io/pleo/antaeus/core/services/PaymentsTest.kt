package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.billing.application.integration.PleoPayments
import io.pleo.antaeus.core.billing.application.models.Bill
import io.pleo.antaeus.core.customers.application.models.CustomerResult
import io.pleo.antaeus.core.invoices.application.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.invoices.application.models.InvoiceResult
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class PaymentsTest {

    private val payments = PleoPayments()

    @Test
    fun `will throw if customer currency does not match invoice currency`() {
        assertThrows<CurrencyMismatchException> {
            payments.charge(
                Bill(
                    InvoiceResult(
                        id = 100,
                        customerId = 1,
                        amount = Money(BigDecimal(100), Currency.DKK),
                        status = InvoiceStatus.PENDING,
                        active = true
                    ),
                    CustomerResult(
                        id = 1,
                        currency = Currency.EUR
                    )
                )
            )
        }
    }

}
