package io.pleo.antaeus.core.customers.application.models

import io.pleo.antaeus.models.Customer

fun Customer.toResult(): CustomerResult = CustomerResult(this.id, this.currency)