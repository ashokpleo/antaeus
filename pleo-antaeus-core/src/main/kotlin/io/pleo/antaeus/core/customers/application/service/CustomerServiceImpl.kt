package io.pleo.antaeus.core.customers.application.service

import com.google.inject.Inject
import io.pleo.antaeus.core.customers.application.api.CustomerService
import io.pleo.antaeus.core.customers.application.models.CustomerResult
import io.pleo.antaeus.core.customers.application.models.toResult
import io.pleo.antaeus.core.customers.application.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.customers.application.exceptions.UnableToCreateCustomerException
import io.pleo.antaeus.data.CustomerRepo
import io.pleo.antaeus.models.Currency

class CustomerServiceImpl @Inject constructor(private val customerRepo: CustomerRepo): CustomerService {
    override fun fetchAll(): List<CustomerResult> {
        return customerRepo.fetchCustomers().map { it.toResult() }
    }

    override fun fetch(id: Int): CustomerResult {
        return customerRepo.fetchCustomer(id)?.toResult() ?: throw CustomerNotFoundException(id)
    }

    override fun createCustomer(currency: Currency): CustomerResult {
        return customerRepo.createCustomer(currency)?.toResult() ?: throw UnableToCreateCustomerException(currency.name)
    }
}