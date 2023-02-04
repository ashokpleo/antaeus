/*
    Implements endpoints related to customers.
 */

package io.pleo.antaeus.core.services

import com.google.inject.Inject
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.data.CustomerRepo
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import kotlin.random.Random

interface CustomerService {
    fun fetchAll(): List<Customer>

    fun fetch(id: Int): Customer

    fun createCustomer(currency: Currency): Customer?
}

class CustomerServiceImpl @Inject constructor(private val customerRepo: CustomerRepo): CustomerService {
    override fun fetchAll(): List<Customer> {
        return customerRepo.fetchCustomers()
    }

    override fun fetch(id: Int): Customer {
        return customerRepo.fetchCustomer(id) ?: throw CustomerNotFoundException(id)
    }

    override fun createCustomer(currency: Currency): Customer? {
        return customerRepo.createCustomer(currency)
    }
}
