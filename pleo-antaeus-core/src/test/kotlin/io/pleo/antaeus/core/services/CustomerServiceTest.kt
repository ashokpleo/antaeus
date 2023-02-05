package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.customers.application.service.CustomerServiceImpl
import io.pleo.antaeus.core.customers.application.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.customers.application.exceptions.UnableToCreateCustomerException
import io.pleo.antaeus.data.CustomerRepo
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerServiceTest {
    private val customerRepo = mockk<CustomerRepo> {
        every { fetchCustomer(404) } returns null
        every { createCustomer(Currency.EUR)} returns null
        every { fetchCustomer(100) } returns Customer(100, Currency.EUR )
        every { fetchCustomers() } returns listOf(Customer(100, Currency.EUR ), Customer(101, Currency.DKK))
    }

    private val customerService = CustomerServiceImpl(customerRepo = customerRepo)

    @Test
    fun `will throw if customer is not found`() {
        assertThrows<CustomerNotFoundException> {
            customerService.fetch(404)
        }
    }

    @Test
    fun `will throw if customer is able to be created`() {
        assertThrows<UnableToCreateCustomerException> {
            customerService.createCustomer(Currency.EUR)
        }
    }

    @Test
    fun `should be able to fetch a single customer`() {
        // When
        val customer = customerService.fetch(100)

        // Then
        assert(customer.id == 100)
        assert(customer.currency == Currency.EUR)
    }

    @Test
    fun `should be able to fetch all customer`() {
        // When
        val customers = customerService.fetchAll()

        // Then
        assert(customers.size == 2)
        assert(customers[0].id == 100)
        assert(customers[0].currency == Currency.EUR)
        assert(customers[1].id == 101)
        assert(customers[1].currency == Currency.DKK)
    }
}
