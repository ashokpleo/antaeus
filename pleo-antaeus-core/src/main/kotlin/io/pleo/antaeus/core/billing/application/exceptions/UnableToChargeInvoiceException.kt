package io.pleo.antaeus.core.billing.application.exceptions

class UnableToChargeInvoiceException(id: Int) : RuntimeException("Invoice $id is not active, Unable to charge")
