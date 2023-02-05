package io.pleo.antaeus.core.invoices.application.exceptions

class CurrencyMismatchException(invoiceId: Int, customerId: Int) :
    Exception("Currency of invoice '$invoiceId' does not match currency of customer '$customerId'")
