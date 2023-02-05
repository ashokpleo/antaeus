package io.pleo.antaeus.core.customers.application.exceptions

class UnableToCreateCustomerException(currency: String) : RuntimeException("Customer with currency $currency")
