package io.pleo.antaeus.core.invoices.application.exceptions

import java.math.BigDecimal

class UnableToCreateInvoiceException(id: Int, amount:BigDecimal) : RuntimeException("Unable to create Invoice for customer $id for $amount")
