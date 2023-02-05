/*
    Implements endpoints related to customers.
 */

package io.pleo.antaeus.core.customers.application.api

import io.pleo.antaeus.core.customers.application.models.CustomerResult
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer

interface CustomerService {
    fun fetchAll(): List<CustomerResult>

    fun fetch(id: Int): CustomerResult

    fun createCustomer(currency: Currency): CustomerResult
}

