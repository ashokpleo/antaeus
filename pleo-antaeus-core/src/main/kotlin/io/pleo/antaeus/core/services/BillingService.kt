package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.Payments

interface BillingService {

}

class BillingServiceImpl(
    private val paymentProvider: Payments
) : BillingService{
// TODO - Add code e.g. here
}
