package io.pleo.antaeus.core.integration

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.integration.models.ChargeResponse
import io.pleo.antaeus.core.spi.Payments
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import kotlin.random.Random

class PleoPayments: Payments {

    override fun charge(invoice: Invoice, customer: Customer?): ChargeResponse {
        if (customer == null)
            throw CustomerNotFoundException(invoice.customerId)
        if (invoice.amount.currency != customer.currency)
            throw CurrencyMismatchException(invoice.id, customer.id)

        // Network Error would be here but won't be adding it

        // Still using a random Boolean so that we can simulate not enough funds or other bank errors
        return ChargeResponse(Random.nextBoolean())
    }
}
