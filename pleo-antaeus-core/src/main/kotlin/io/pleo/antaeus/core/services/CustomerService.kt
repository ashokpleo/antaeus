/*
    Implements endpoints related to customers.
 */

package io.pleo.antaeus.core.services

import com.google.inject.Inject
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import kotlin.random.Random

interface CustomerService {
    fun fetchAll(): List<Customer>

    fun fetch(id: Int): Customer
    
    fun createCustomer(): Customer?
}

class CustomerServiceImpl @Inject constructor(private val dal: AntaeusDal): CustomerService {
    override fun fetchAll(): List<Customer> {
        return dal.fetchCustomers()
    }

    override fun fetch(id: Int): Customer {
        return dal.fetchCustomer(id) ?: throw CustomerNotFoundException(id)
    }

    override fun createCustomer(): Customer? {
        return dal.createCustomer(
            currency = Currency.values()[Random.nextInt(0, Currency.values().size)]
        )
    }
}
