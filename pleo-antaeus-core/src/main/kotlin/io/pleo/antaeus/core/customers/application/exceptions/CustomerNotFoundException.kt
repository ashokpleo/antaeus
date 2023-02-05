package io.pleo.antaeus.core.customers.application.exceptions

import io.pleo.antaeus.core.common.exceptions.EntityNotFoundException

class CustomerNotFoundException(id: Int) : EntityNotFoundException("Customer", id)
