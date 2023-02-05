package io.pleo.antaeus.core.billing.application.models

fun Bill.toResult(): BillResult = BillResult(this.invoice, this.customer, this.chargeSucceeded)