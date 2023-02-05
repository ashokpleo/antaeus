package io.pleo.antaeus.core.billing.application.integration

import io.pleo.antaeus.core.invoices.application.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.customers.application.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.billing.application.integration.models.ChargeResponse
import io.pleo.antaeus.core.billing.application.spi.Payments
import io.pleo.antaeus.core.billing.application.models.Bill
import kotlin.random.Random

class PleoPayments: Payments {

    override fun charge(bill: Bill): ChargeResponse {
        if (bill.customer == null)
            throw CustomerNotFoundException(bill.invoice.customerId)
        if (bill.invoice.amount.currency != bill.customer.currency)
            throw CurrencyMismatchException(bill.invoice.id, bill.customer.id)

        // Network Error would be here but won't be adding it

        // Still using a random Boolean so that we can simulate not enough funds or other bank errors
        return ChargeResponse(Random.nextBoolean())
    }
}
